package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.access.*;
import dev.tr7zw.firstperson.api.*;
import dev.tr7zw.firstperson.config.*;
import dev.tr7zw.firstperson.modsupport.*;
import dev.tr7zw.firstperson.versionless.*;
import dev.tr7zw.transition.loader.*;
import lombok.*;
import net.minecraft.client.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.resources.*;

public abstract class FirstPersonModelCore extends FirstPersonBase {

    @Getter
    private LogicHandler logicHandler;
    public static FirstPersonModelCore instance;
    private boolean isHeld = false;
    private KeyMapping keyBinding = new KeyMapping("key.firstperson.toggle", 295,
            //? if >= 1.21.9 {
            new KeyMapping.Category(/*? if >=1.21.11 {*/ Identifier
                    /*?} else {*//*ResourceLocation*//*?}*/.fromNamespaceAndPath("firstperson", "keybind")));
    //? } else {
    /*"firstperson.keybind");
    *///? }
    private boolean lateInit = true;
    @Deprecated
    public static boolean enabled = true;
    @Deprecated
    public static boolean isRenderingPlayer = false;
    private CameraType lastCameraType = null;

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

        //? if neoforge {
        /*
                ModLoaderUtil.registerForgeEvent(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
        *///? }
           //? if forge {
           /*
           //? if >= 1.21.6 {
           
           net.minecraftforge.client.event.RenderHandEvent.BUS
                   .addListener(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
           //? } else {
           
           //        ModLoaderUtil.registerForgeEvent(new dev.tr7zw.firstperson.forge.RenderHandEventListener()::onRender);
           //? }
           *///? }

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
        updatePlayerLayers();
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
        // TODO: ugly sideffect, find better way
        if (lastCameraType != Minecraft.getInstance().options.getCameraType()) {
            lastCameraType = Minecraft.getInstance().options.getCameraType();
            updatePlayerLayers();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        FirstPersonModelCore.enabled = enabled;
    }

    public void updatePlayerLayers() {
        PlayerRendererAccess access = null;
        //? if >= 1.21.6 {

        access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(
                /*? if >= 1.21.9 {*/new AvatarRenderState()/*?} else {*//*new PlayerRenderState()*//*? }*/);
        //? } else {
        /*
        if (Minecraft.getInstance().player != null) {
            access = (PlayerRendererAccess) Minecraft.getInstance().getEntityRenderDispatcher()
                    .getRenderer(Minecraft.getInstance().player);
        }
        *///? }
        if (access != null) {
            access.updatePartsList(lastCameraType != CameraType.FIRST_PERSON);
        }
    }

}
