package dev.tr7zw.firstperson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.config.CustomConfigScreen;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.config.FirstPersonSettings;
import dev.tr7zw.firstperson.modsupport.PlayerAnimatorSupport;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;

public abstract class FirstPersonModelCore {

    public static final Logger LOGGER = LogManager.getLogger("FirstPersonModel");
    public static MinecraftWrapper wrapper;
    public static FirstPersonModelCore instance;
    public static boolean isRenderingPlayer = false;
    public static boolean enabled = true;
    public static FirstPersonSettings config = null;
    protected static boolean isHeld = false;
    public static KeyMapping keyBinding = new KeyMapping("key.firstperson.toggle", 295, "Firstperson");
    private File settingsFile = new File("config", "firstperson.json");
    private boolean lateInit = true;

    public static final float sneakBodyOffset = 0.27f;
    public static final float swimUpBodyOffset = 0.60f;
    public static final float swimDownBodyOffset = 0.50f;
    public static final float inVehicleBodyOffset = 0.20f;

    public void sharedSetup() {
        LOGGER.info("Loading FirstPerson Mod");
        wrapper = new MinecraftWrapper(Minecraft.getInstance());

        if (settingsFile.exists()) {
            try {
                config = new Gson().fromJson(
                        new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8),
                        FirstPersonSettings.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (config == null) {
            config = new FirstPersonSettings();
        }
        writeSettings();
        enabled = config.enabledByDefault;

        registerKeybinds();
    }

    public abstract void registerKeybinds();

    public static MinecraftWrapper getWrapper() {
        return wrapper;
    }

    public static boolean fixBodyShadow(PoseStack matrixStack) {
        return (enabled && (config.forceActive || FirstPersonModelCore.isRenderingPlayer));
    }

    private void lateInit() {
        try {
            if (isValidClass("dev.kosmx.playerAnim.core.impl.AnimationProcessor")) {
                LOGGER.info("Loading PlayerAnimator support!");
                FirstPersonAPI.registerPlayerHandler(new PlayerAnimatorSupport());
            } else {
                LOGGER.info("PlayerAnimator not found!");
            }
        } catch (Throwable ex) {
            LOGGER.warn("Error during initialization of mod support.", ex);
        }
    }

    public void onTick() {
        if (lateInit) {
            lateInit = false;
            lateInit();
        }
        if (keyBinding.isDown()) {
            if (isHeld)
                return;
            isHeld = true;
            enabled = !enabled;
        } else {
            isHeld = false;
        }
    }

    public void writeSettings() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(config);
        try {
            Files.write(settingsFile.toPath(), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen createConfigScreen(Screen parent) {
        CustomConfigScreen screen = new CustomConfigScreen(parent, "text.firstperson.title") {

            @Override
            public void initialize() {
                getOptions().addBig(getOnOffOption("text.firstperson.option.firstperson.enabledByDefault",
                        () -> config.enabledByDefault, (b) -> config.enabledByDefault = b));

                List<OptionInstance<?>> options = new ArrayList<>();
                options.add(getIntOption("text.firstperson.option.firstperson.xOffset", -40, 40, () -> config.xOffset,
                        (i) -> config.xOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sneakXOffset", -40, 40,
                        () -> config.sneakXOffset, (i) -> config.sneakXOffset = i));
                options.add(getIntOption("text.firstperson.option.firstperson.sitXOffset", -40, 40,
                        () -> config.sitXOffset, (i) -> config.sitXOffset = i));
                options.add(getOnOffOption("text.firstperson.option.firstperson.renderStuckFeatures",
                        () -> config.renderStuckFeatures, (b) -> config.renderStuckFeatures = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.vanillaHands",
                        () -> config.vanillaHands, (b) -> config.vanillaHands = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.doubleHands", () -> config.doubleHands,
                        (b) -> config.doubleHands = b));
                options.add(getOnOffOption("text.firstperson.option.firstperson.forceActive", () -> config.forceActive,
                        (b) -> config.forceActive = b));

                getOptions().addSmall(options.toArray(new OptionInstance[0]));

            }

            @Override
            public void save() {
                writeSettings();
            }

            @Override
            public void reset() {
                config = new FirstPersonSettings();
                writeSettings();
            }

        };

        return screen;
    }

    /**
     * Checks if a class exists or not
     * 
     * @param name
     * @return
     */
    protected static boolean isValidClass(String name) {
        try {
            if (Class.forName(name) != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
        }
        return false;
    }

}
