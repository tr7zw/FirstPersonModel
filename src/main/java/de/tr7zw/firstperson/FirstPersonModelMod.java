package de.tr7zw.firstperson;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class FirstPersonModelMod implements ModInitializer {
	
	//Helper var
	public static boolean hideNextHeadArmor = false;
	public static boolean isRenderingPlayer = false;
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	public static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll
	public static boolean enabled = true;
	public static FirstPersonConfig config = null;
	private static KeyBinding keyBinding;
	private static boolean isHeld = false;


	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (!enabled || config.improvedCompatibility && !(hideHeadWithMatrixStack == matrixStack));
	}


	public static final float sneakBodyOffset = 0.27f;
	public static final float swimUpBodyOffset = 0.60f;
	public static final float swimDownBodyOffset = 0.50f;
	public static final float inVehicleBodyOffset = 0.20f;


	public static boolean isFixActive(Entity player, MatrixStack matrices){
		return MinecraftClient.getInstance() != null && MinecraftClient.getInstance().getCameraEntity() == player && (matrices == hideHeadWithMatrixStack || config.forceActive && matrices != paperDollStack);
	}
	
	@Override
	public void onInitialize() {
		System.out.println("Loaded FirstPerson Models");
		AutoConfig.register(FirstPersonConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(FirstPersonConfig.class).getConfig();
		enabled = config.enabledByDefault;
	    keyBinding = new KeyBinding(
	            new Identifier("firstperson", "toggle").getPath(),
	            net.minecraft.client.util.InputUtil.Type.KEYSYM,
	            GLFW.GLFW_KEY_F6,
	            "Firstperson"
	        );
	    //KeyBindingRegistry.INSTANCE.addCategory("Firstperson");  It is added automatically now
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
	}
}
