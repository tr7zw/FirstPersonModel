package dev.tr7zw.firstperson.fabric;

import javax.annotation.Nullable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class FirstPersonModelMod extends FirstPersonModelCore implements ModInitializer {
	
	public static boolean hasOptifabric = false;

	public FirstPersonModelMod() {
		instance = this;
	}

	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || FirstPersonModelCore.isRenderingPlayer));
	}


	
	@Override
	public void onInitialize() {
		wrapper = new FabricWrapper(MinecraftClient.getInstance());
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	    	onTick();
	    });
	    sharedSetup();
	    if(FabricLoader.getInstance().isModLoaded("optifabric")) {
	    	hasOptifabric = true;
	    	System.out.println("Found optifabric, limiting 3rd party mod compatebility!");
	    }
	}

}
