package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.config.ConfigScreenProvider;
import dev.tr7zw.firstperson.modsupport.ModSupportLoader;
import dev.tr7zw.firstperson.modsupport.PlayerAnimatorSupport;
import dev.tr7zw.firstperson.versionless.FirstPersonBase;
import dev.tr7zw.transition.loader.ModLoaderEventUtil;
import dev.tr7zw.transition.loader.ModLoaderUtil;
import lombok.Getter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public abstract class FirstPersonModelCore extends FirstPersonBase {

    @Getter
    private LogicHandler logicHandler;
    public static FirstPersonModelCore instance;
    private boolean isHeld = false;
    private KeyMapping keyBinding = new KeyMapping("key.firstperson.toggle", 295, "firstperson.keybind");
    private boolean lateInit = true;
    @Deprecated
    public static boolean enabled = true;
    @Deprecated
    public static boolean isRenderingPlayer = false;

    protected FirstPersonModelCore() {
        instance = this;
    }

    public void sharedSetup() {
        LOGGER.info("Loading FirstPerson Mod");
        logicHandler = new LogicHandler(Minecraft.getInstance(), this);

        super.loadConfig();

        ModLoaderUtil.registerKeybind(keyBinding);
        ModLoaderEventUtil.registerClientTickStartListener(this::onTick);
        ModLoaderUtil.disableDisplayTest();
        ModLoaderUtil.registerConfigScreen(ConfigScreenProvider::createConfigScreen);

        //#if NEOFORGE
        //$$        ModLoaderUtil.registerForgeEvent(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
        //#endif
        //#if FORGE
        //#if MC >= 12106
        //$$ net.minecraftforge.client.event.RenderHandEvent.BUS.addListener(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
        //#else
        //$$        ModLoaderUtil.registerForgeEvent(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
        //#endif
        //#endif

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
            if (isHeld) {
                return;
            }
            isHeld = true;
            setEnabled(!isEnabled());
        } else {
            isHeld = false;
        }
    }

    @Override
    public void setRenderingPlayer(boolean isRenderingPlayer) {
        super.setRenderingPlayer(isRenderingPlayer);
        FirstPersonModelCore.isRenderingPlayer = isRenderingPlayer;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        FirstPersonModelCore.enabled = enabled;
    }

}
