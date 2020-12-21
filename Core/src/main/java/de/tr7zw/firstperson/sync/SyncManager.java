package de.tr7zw.firstperson.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.config.CosmeticSettings.SyncSnapshot;

@SuppressWarnings("deprecation")
public class SyncManager implements Runnable {

	private SyncSnapshot settings = null;
	private Gson gson = new Gson();
	private Map<UUID, PlayerSettings> playersToUpdate = new ConcurrentHashMap<>();
	private final String settingsUrl;
	private MinecraftWrapper wrapper = FirstPersonModelCore.instance.getWrapper();
	private Thread thread;
	private boolean unreachable = false;

	public SyncManager() {
			settingsUrl = FirstPersonModelCore.APIHost + "/firstperson/settings/";
			this.thread = new Thread(this);
			thread.start();
	}
	
	// Works on the queue
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(!playersToUpdate.isEmpty()) {
				String request = gson.toJson(playersToUpdate.keySet().stream().map(u -> u.toString()).limit(10).toArray());
				try {
					String json = performPost(settingsUrl, request);
					RequestedSettings set = gson.fromJson(json, RequestedSettings.class);
					for(Entry<String, SyncSnapshot> ent : set.data.entrySet()) {
						PlayerSettings player = playersToUpdate.remove(UUID.fromString(ent.getKey()));
						if(player != null) {
							player.setCosmeticSettings(ent.getValue().toSettings());
						}
					}
				} catch (Exception e) {
					playersToUpdate.clear();
					e.printStackTrace();
				}
			}
		}
	}
	
	public void takeSnapshot() {
		settings = FirstPersonModelCore.config.cosmetic.createSnapshot();
	}
	
	public void checkForUpdates() {
		if(unreachable)return; // Not authenticated, don't even bother mojang
		if (settings == null) {
			settings = FirstPersonModelCore.config.cosmetic.createSnapshot();
		}
		SyncSnapshot tmp = FirstPersonModelCore.config.cosmetic.createSnapshot();
		if (!tmp.equals(settings)) {
			settings = tmp;
			String random = UUID.randomUUID().toString();
			String sha = hash("verify-" + random + ".tr7zw.dev");
			String request = wrapper.joinServerSession(sha);
			if (request == null) { // Authenticated user
				String content = gson.toJson(settings);
				try {
					String ret = performPost(FirstPersonModelCore.APIHost + "/firstperson/update/" + wrapper.getGameprofile().getName() + "/" + random, content);
					if("{\"status\":\"OK\"}".equals(ret)) {
						wrapper.showToastSuccess("Firstperson updated!", null);
						wrapper.sendNoLayerClientSettings();
					}else {
						wrapper.showToastFailure("Firstperson failed!", ret);
					}
				} catch (Exception e) {
					e.printStackTrace();
					wrapper.showToastFailure("Firstperson failed!", "Error while reaching the server");
				}
			} else {
				unreachable = true;
				wrapper.showToastFailure("Firstperson failed!", request);
			}
		}
	}
	
	private String performPost(String url, String content) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        try (
		CloseableHttpClient client = HttpClientBuilder.create().setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
				// have to kill the Certificate check because .dev domains must use SSL, but Minecraft's SSL context seems to only trust Mojang certs
				return true;
			}
		}).build()).build()) {

        	HttpPost request = new HttpPost(url);
            request.setHeader("User-Agent", "FirstPersonMod");
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(content));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {

                builder.append(line);
            }

            return builder.toString();
        }
		
	}
	
	public void updateSettings(PlayerSettings settings) {
		playersToUpdate.put(settings.getUUID(), settings);
	}

	private static String hash(String str) {
		try {
			byte[] digest = digest(str, "SHA-1");
			return new BigInteger(digest).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] digest(String str, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
		return md.digest(strBytes);
	}
	
    public static class RequestedSettings{
    	public Map<String, SyncSnapshot> data = new HashMap<>();
    }

}
