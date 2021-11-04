package dev.tr7zw.firstperson.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class FirstPersonModelMod extends FirstPersonModelCore implements ModInitializer {
	
	public static boolean hasOptifabric = false;

	public FirstPersonModelMod() {
		instance = this;
	}

	public static boolean fixBodyShadow(PoseStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || FirstPersonModelCore.isRenderingPlayer));
	}
	
	@Override
	public void onInitialize() {
		wrapper = new FabricWrapper(Minecraft.getInstance());
	    sharedSetup();
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	    	onTick();
	    });
	    if(FabricLoader.getInstance().isModLoaded("optifabric")) {
	    	hasOptifabric = true;
	    	System.out.println("Found optifabric, limiting 3rd party mod compatebility!");
	    }
	}

    @Override
    public void registerKeybinds() {
        KeyBindingHelper.registerKeyBinding(keyBinding);
    }

}
