package dev.tr7zw.firstperson;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;

import dev.tr7zw.firstperson.config.FirstPersonConfig;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureProvider;
import dev.tr7zw.firstperson.sync.SyncManager;
import dev.tr7zw.transliterationlib.api.wrapper.util.Keybind;

public abstract class FirstPersonModelCore {

	public static MinecraftWrapper wrapper;
	public static FirstPersonModelCore instance;
	public static boolean isRenderingPlayer = false;
	public static boolean enabled = true;
	public static FirstPersonConfig config = null;
	protected static boolean isHeld = false;
	public static SyncManager syncManager;
	public static final String APIHost = "https://firstperson.tr7zw.dev";
	public static Keybind keyBinding;
	
	public static final float sneakBodyOffset = 0.27f;
	public static final float swimUpBodyOffset = 0.60f;
	public static final float swimDownBodyOffset = 0.50f;
	public static final float inVehicleBodyOffset = 0.20f;
	
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
		syncManager.takeSnapshot();
		keyBinding = transliteration.constructors().newKeybind("toggle", 295, "Firstperson");
		transliteration.getKeybindings().registerKeybinding(keyBinding);
		FeatureProvider.getFeatures().forEach(AbstractCosmetic::initTextures);
	}
	
	public static MinecraftWrapper getWrapper() {
		return wrapper;
	}
	public abstract boolean isFixActive(Object player, Object matrices);
	public abstract boolean isCompatebilityMatrix(Object entity, Object matrices);

	public void onTick() {
        if(keyBinding.isPressed()) {
        	if(isHeld)return;
        	isHeld = true;
        	enabled = !enabled;
        }else {
        	isHeld = false;
        }
	}
	
}
