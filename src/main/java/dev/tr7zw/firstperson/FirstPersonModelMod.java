package dev.tr7zw.firstperson;

import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.modsupport.FreecamSupport;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FirstPersonModelMod extends FirstPersonModelCore implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        sharedSetup();
        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            onTick();
        });
        try {
            if (FabricLoader.getInstance().isModLoaded("freecam")) {
                FirstPersonAPI.registerPlayerHandler(new FreecamSupport());
                FirstPersonModelCore.LOGGER.info("Freecam support loaded.");
            }
        } catch (Throwable ex) {
            FirstPersonModelCore.LOGGER.warn("Error during initialization of mod support.", ex);
        }
    }

    @Override
    public void registerKeybinds() {
        KeyBindingHelper.registerKeyBinding(keyBinding);
    }

}
