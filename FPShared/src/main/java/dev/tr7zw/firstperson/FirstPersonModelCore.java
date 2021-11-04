package dev.tr7zw.firstperson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.config.FirstPersonConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public abstract class FirstPersonModelCore {

    public static MinecraftWrapper wrapper;
    public static FirstPersonModelCore instance;
    public static boolean isRenderingPlayer = false;
    public static boolean enabled = true;
    public static FirstPersonConfig config = null;
    protected static boolean isHeld = false;
    public static KeyMapping keyBinding = new KeyMapping("toggle", 295, "Firstperson");

    public static final float sneakBodyOffset = 0.27f;
    public static final float swimUpBodyOffset = 0.60f;
    public static final float swimDownBodyOffset = 0.50f;
    public static final float inVehicleBodyOffset = 0.20f;

    public void sharedSetup() {
        System.out.println("Loading FirstPerson Mod");
        wrapper = new MinecraftWrapper(Minecraft.getInstance());
        File settingsFile = new File("config", "firstperson.json");
        if (settingsFile.exists()) {
            try {
                config = new Gson().fromJson(
                        new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8),
                        FirstPersonConfig.class);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (config == null) {
            config = new FirstPersonConfig();
        }
        enabled = config.firstPerson.enabledByDefault;

        registerKeybinds();
    }

    public abstract void registerKeybinds();

    public static MinecraftWrapper getWrapper() {
        return wrapper;
    }

    public static boolean fixBodyShadow(PoseStack matrixStack) {
        return (enabled && (config.firstPerson.forceActive || FirstPersonModelCore.isRenderingPlayer));
    }

    public void onTick() {
        if (keyBinding.isDown()) {
            if (isHeld)
                return;
            isHeld = true;
            enabled = !enabled;
        } else {
            isHeld = false;
        }
    }

}
