package dev.tr7zw.firstperson.fabric;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FirstPersonModelMod extends FirstPersonModelCore implements ModInitializer {
	
	public static boolean hasOptifabric = false;

	public FirstPersonModelMod() {
		instance = this;
	}
	
	@Override
	public void onInitialize() {
	    sharedSetup();
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	    	onTick();
	    });
	    if(FabricLoader.getInstance().isModLoaded("optifabric")) {
	    	hasOptifabric = true;
	    	System.out.println("Found optifabric, limiting 3rd party mod compatebility!"); // TODO does nothing^^
	    }
	}

    @Override
    public void registerKeybinds() {
        KeyBindingHelper.registerKeyBinding(keyBinding);
    }

}
