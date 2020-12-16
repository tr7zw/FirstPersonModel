package de.tr7zw.firstperson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;

import de.tr7zw.firstperson.config.FirstPersonConfig;
import de.tr7zw.firstperson.sync.SyncManager;

public abstract class FirstPersonModelCore {

	public static FirstPersonModelCore instance;
	public static boolean hideNextHeadArmor = false;
	public static boolean isRenderingPlayer = false;
	public static boolean enabled = true;
	public static FirstPersonConfig config = null;
	protected static boolean isHeld = false;
	public static SyncManager syncManager;
	public static final String APIHost = "https://firstperson.tr7zw.dev";
	
	public abstract MinecraftWrapper getWrapper();
	
	public void sharedSetup() {
		System.out.println("Loading FirstPerson Mod");
		File settingsFile = new File("config", "firstperson.json");
		if(settingsFile.exists()) {
			try {
				config = new Gson().fromJson(new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8), FirstPersonConfig.class);
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		if(config == null) {
			config = new FirstPersonConfig();
		}
		enabled = config.firstPerson.enabledByDefault;
		syncManager = new SyncManager();
	}
	
}
