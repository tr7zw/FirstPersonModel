package dev.tr7zw.firstperson.fabric;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.velvet.api.Velvet;
import dev.tr7zw.velvet.fabric.VelvetImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FirstPersonModelMod extends FirstPersonModelCore implements ModInitializer {
	
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	public static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll
	private static KeyBinding keyBinding;

	public FirstPersonModelMod() {
		instance = this;
	}

	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (enabled && (config.firstPerson.forceActive || hideHeadWithMatrixStack == matrixStack));
	}


	public boolean isFixActive(Object player, Object matrices){
		return MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getCameraEntity() == player && (matrices == hideHeadWithMatrixStack || config.firstPerson.forceActive && matrices != paperDollStack);
	}
	
	@Override
	public void onInitialize() {
		wrapper = new FabricWrapper(MinecraftClient.getInstance());
		Velvet.velvet = new VelvetImpl();
	    keyBinding = new KeyBinding(
	            new Identifier("firstperson", "toggle").getPath(),
	            net.minecraft.client.util.InputUtil.Type.KEYSYM,
	            GLFW.GLFW_KEY_F6,
	            "Firstperson"
	        );
	    KeyBindingHelper.registerKeyBinding(keyBinding);
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	        if(keyBinding.isPressed()) {
	        	if(isHeld)return;
	        	isHeld = true;
	        	enabled = !enabled;
	        }else {
	        	isHeld = false;
	        }
	    });
	    sharedSetup();
	}
	  
}
