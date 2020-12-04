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
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;

import de.tr7zw.firstperson.FirstPersonConfig.SyncSnapshot;
import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SyncManager implements Runnable {

	private SyncSnapshot settings = null;
	private Gson gson = new Gson();
	private Map<UUID, PlayerSettings> playersToUpdate = new ConcurrentHashMap<>();
	private final String settingsUrl;
	private Thread thread;
	private MinecraftClient client = MinecraftClient.getInstance();
	private boolean updateNextTick = false;

	public SyncManager() {
			settingsUrl = FirstPersonModelMod.APIHost + "/firstperson/settings/";
			thread = new Thread(this, "Firstperson sync thread");
			thread.start();
	}
	
	@Override
	public void run() {
		while(client.isRunning()) { 
			try {
				Thread.sleep(1000);
				checkForUpdates();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void checkForUpdates() {
		if (settings == null) {
			settings = FirstPersonModelMod.config.createSnapshot();
			return;
		}
		if(updateNextTick) {
			updateNextTick = false;
			client.options.onPlayerModelPartChange();
		}
		SyncSnapshot tmp = FirstPersonModelMod.config.createSnapshot();
		if (!tmp.equals(settings)) { // there has to be a better way
			settings = tmp;
			String random = UUID.randomUUID().toString();
			String sha = hash("verify-" + random + ".tr7zw.dev");
			Text request = joinServerSession(sha);
			if (request == null) { // Authenticated user
				String content = gson.toJson(Settings.fromSnapshot(settings));
				try {
					String ret = performPost(FirstPersonModelMod.APIHost + "/firstperson/update/" + client.getSession().getProfile().getName()+ "/" + random, content);
					if("{\"status\":\"OK\"}".equals(ret)) {
						client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP, new LiteralText("Firstperson updated!"), null));
						GameOptions options = client.options;
						//this blinks the outer layer once, signaling a reload of this player
						if(this.client.player != null && this.client.player.networkHandler != null)
							this.client.player.networkHandler.sendPacket(new ClientSettingsC2SPacket(options.language, options.viewDistance,
									options.chatVisibility, options.chatColors, 0, options.mainArm));
						updateNextTick = true;
					}else {
						client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, new LiteralText("Firstperson failed!"), new LiteralText(ret)));
					}
				} catch (Exception e) {
					e.printStackTrace();
					client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, new LiteralText("Firstperson failed!"), new LiteralText("Error while reaching the server")));
				}
			} else {
				client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, new LiteralText("Firstperson failed!"), new LiteralText(request.getString())));
			}
		}
		if(!playersToUpdate.isEmpty()) {
			String request = gson.toJson(playersToUpdate.keySet().stream().map(u -> u.toString()).limit(10).toArray());
			try {
				String json = performPost(settingsUrl, request);
				RequestedSettings set = gson.fromJson(json, RequestedSettings.class);
				for(Entry<String, Settings> ent : set.data.entrySet()) {
					PlayerSettings player = playersToUpdate.remove(UUID.fromString(ent.getKey()));
					if(player != null) {
						player.setCustomHeight(ent.getValue().height);
						player.setChest(Chest.getChest(ent.getValue().chest));
						player.setHat(Hat.getHat(ent.getValue().hat));
					}
				}
			} catch (Exception e) {
				playersToUpdate.clear();
				e.printStackTrace();
			}
		}
	}
	
	private String performPost(String url, String content) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        try (CloseableHttpClient client = HttpClientBuilder.create().setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			
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

	private Text joinServerSession(String var1) {
		try {
			client.getSessionService().joinServer(
					client.getSession().getProfile(),
					client.getSession().getAccessToken(), var1);
		} catch (AuthenticationUnavailableException var3) {
			return new TranslatableText("disconnect.loginFailedInfo",
					new Object[] { new TranslatableText("disconnect.loginFailedInfo.serversUnavailable") });
		} catch (InvalidCredentialsException var4) {
			return new TranslatableText("disconnect.loginFailedInfo",
					new Object[] { new TranslatableText("disconnect.loginFailedInfo.invalidSession") });
		} catch (AuthenticationException var5) {
			return new TranslatableText("disconnect.loginFailedInfo", new Object[] { var5.getMessage() });
		}

		return null;
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
    	public Map<String, Settings> data = new HashMap<>();
    }
	
    public static class Settings{
    	public int height = 100;
    	public int chest = 0;
    	public int boots = 0;
    	public int head = 0;
    	public int back = 0;
    	public int hat = 0;
		@Override
		public String toString() {
			return "Settings [height=" + height + ", chest=" + chest + ", boots=" + boots + ", head=" + head + ", back="
					+ back + ", hat=" + hat + "]";
		}
		
		public static Settings fromSnapshot(SyncSnapshot rec) {
			Settings settings = new Settings();
			settings.height = rec.height;
			settings.chest = rec.chest;
			settings.boots = rec.boots;
			settings.head = rec.head;
			settings.back = rec.back;
			settings.back = rec.hat;
			return settings;
		}
    }

}
