package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.api.ActivationHandler;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.modsupport.PlayerAnimatorSupport;
import dev.tr7zw.firstperson.versionless.FirstPersonBase;
import lombok.Getter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public abstract class FirstPersonModelCore extends FirstPersonBase {

    @Getter
    private LogicHandler logicHandler;
    public static FirstPersonModelCore instance;
    private boolean isHeld = false;
    protected KeyMapping keyBinding = new KeyMapping("key.firstperson.toggle", 295, "Firstperson");
    private boolean lateInit = true;

    public FirstPersonModelCore() {
        instance = this;
    }

    public void sharedSetup() {
        LOGGER.info("Loading FirstPerson Mod");
        logicHandler = new LogicHandler(Minecraft.getInstance());

        super.loadConfig();

        registerKeybinds();
    }

    public abstract void registerKeybinds();

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
        logicHandler.clearAutoVanillaHandsList();
        Item invalid = BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", "air"));
        for (String itemId : config.autoVanillaHands) {
            try {
                Item item = BuiltInRegistries.ITEM
                        .get(new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
                if (invalid != item)
                    logicHandler.addAutoVanillaHandsItem(item);
            } catch (Exception ex) {
                LOGGER.info("Unknown item to add to the auto vanilla hold list: " + itemId);
            }
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

}
