package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.config.ConfigScreenProvider;
import dev.tr7zw.firstperson.modsupport.ModSupportLoader;
import dev.tr7zw.firstperson.modsupport.PlayerAnimatorSupport;
import dev.tr7zw.firstperson.versionless.FirstPersonBase;
import dev.tr7zw.util.ModLoaderUtil;
import lombok.Getter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public abstract class FirstPersonModelCore extends FirstPersonBase {

    @Getter
    private LogicHandler logicHandler;
    public static FirstPersonModelCore instance = new Fir;
    private boolean isHeld = false;
    private KeyMapping keyBinding = new KeyMapping("key.firstperson.toggle", 295, "Firstperson");
    private boolean lateInit = true;

    protected FirstPersonModelCore() {
        instance = this;
        ModLoaderUtil.disableDisplayTest();
        ModLoaderUtil.registerConfigScreen(ConfigScreenProvider::createConfigScreen);
        ModLoaderUtil.registerClientSetupListener(this::sharedSetup);
    }

    public void sharedSetup() {
        LOGGER.info("Loading FirstPerson Mod");
        logicHandler = new LogicHandler(Minecraft.getInstance(), this);

        super.loadConfig();

        ModLoaderUtil.registerKeybind(keyBinding);
        ModLoaderUtil.registerClientTickListener(this::onTick);

        ModSupportLoader.loadSupport();
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

        logicHandler.registerDefaultHandlers();
        logicHandler.reloadAutoVanillaHandsSettings();
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
            setEnabled(!isEnabled());
        } else {
            isHeld = false;
        }
    }

}
