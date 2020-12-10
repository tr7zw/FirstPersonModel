package de.tr7zw.firstperson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;

import de.tr7zw.firstperson.config.FirstPersonConfig;
import de.tr7zw.firstperson.sync.SyncManager;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class FirstPersonModelMod implements ModInitializer {
	
	//Helper var
	public static boolean hideNextHeadArmor = false;
	public static boolean isRenderingPlayer = false;
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	public static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll
	public static boolean enabled = true;
	public static FirstPersonConfig config = null;
	private static KeyBinding keyBinding;
	private static boolean isHeld = false;
	public static SyncManager syncManager;
	public static final String APIHost = "https://firstperson.tr7zw.dev";


	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || hideHeadWithMatrixStack == matrixStack));
	}


	public static final float sneakBodyOffset = 0.27f;
	public static final float swimUpBodyOffset = 0.60f;
	public static final float swimDownBodyOffset = 0.50f;
	public static final float inVehicleBodyOffset = 0.20f;


	public static boolean isFixActive(Entity player, MatrixStack matrices){
		return MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getCameraEntity() == player && (matrices == hideHeadWithMatrixStack || config.firstPerson.forceActive && matrices != paperDollStack);
	}
	
	@Override
	public void onInitialize() {
		System.out.println("Loaded FirstPerson Models");
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
	    keyBinding = new KeyBinding(
	            new Identifier("firstperson", "toggle").getPath(),
	            net.minecraft.client.util.InputUtil.Type.KEYSYM,
	            GLFW.GLFW_KEY_F6,
	            "Firstperson"
	        );
	    KeyBindingHelper.registerKeyBinding(keyBinding);
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	        if(keyBinding.isPressed()) {
	        	if(isHeld)return;
	        	isHeld = true;
	        	enabled = !enabled;
	        }else {
	        	isHeld = false;
	        }
	    });
	   syncManager = new SyncManager();
	}
	

	   
}
