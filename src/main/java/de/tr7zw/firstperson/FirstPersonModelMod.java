package de.tr7zw.firstperson;

import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class FirstPersonModelMod implements ModInitializer {
	
	//Helper var
	public static boolean hideNextHeadArmor = false;
	public static boolean isRenderingPlayer = false;
	@Nullable
	public static MatrixStack hideHeadWithMatrixStack = null;
	public static boolean enabled = true;
	public static FirstPersonConfig config = null;
	private static FabricKeyBinding keyBinding;
	private static boolean isHeld = false;

	public static boolean fixBodyShadow(MatrixStack matrixStack){
		return (!enabled || config.improvedCompatibility && !(hideHeadWithMatrixStack == matrixStack));
	}


	public static final float sneakBodyOffset = 0.27f;
	public static final float swimUpBodyOffset = 0.60f;
	public static final float swimDownBodyOffset = 0.50f;
	public static final float inVehicleBodyOffset = 0.20f;

	
	@Override
	public void onInitialize() {
		System.out.println("Loaded FirstPerson Models");
		AutoConfig.register(FirstPersonConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(FirstPersonConfig.class).getConfig();
		enabled = config.enabledByDefault;
	    keyBinding = FabricKeyBinding.Builder.create(
	            new Identifier("firstperson", "toggle"),
	            net.minecraft.client.util.InputUtil.Type.KEYSYM,
	            GLFW.GLFW_KEY_F6,
	            "Firstperson"
	        ).build();
	    KeyBindingRegistry.INSTANCE.addCategory("Firstperson");
	    KeyBindingRegistry.INSTANCE.register(keyBinding);
	    ClientTickCallback.EVENT.register(e ->
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
