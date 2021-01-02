package dev.tr7zw.firstperson.fabric;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.velvet.api.Velvet;
import dev.tr7zw.velvet.fabric.VelvetImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;

public class FirstPersonModelMod extends FirstPersonModelCore implements ModInitializer {
	
	@Nullable
	private static MatrixStack hideHeadWithMatrixStack = null;
	@Nullable
	private static MatrixStack paperDollStack = null; //Make force compatibility not hide paper doll
	private Set<EntityType<?>> disallowedTypes = new HashSet<>();

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
	    ClientTickEvents.END_CLIENT_TICK.register(e ->
	    {
	    	onTick();
	    });
	    disallowedTypes.add(EntityType.IRON_GOLEM);
	    disallowedTypes.add(EntityType.HOGLIN);
	    disallowedTypes.add(EntityType.ZOGLIN);
	    disallowedTypes.add(EntityType.ZOMBIFIED_PIGLIN); //Not working correctly
	    sharedSetup();
	}

	@Override
	public boolean isCompatebilityMatrix(Object entity, Object matrices) {
		return MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != entity && (matrices == hideHeadWithMatrixStack && matrices != paperDollStack);
	}
	
	public static void clearHeadStack() {
		setHideHeadWithMatrixStack(null);
	}

	public static void setHideHeadWithMatrixStack(MatrixStack hideHeadWithMatrixStack) {
		FirstPersonModelMod.hideHeadWithMatrixStack = hideHeadWithMatrixStack;
	}

	public static void setPaperDollStack(MatrixStack paperDollStack) {
		FirstPersonModelMod.paperDollStack = paperDollStack;
	}

	@Override
	public boolean isDisallowedEntityType(Object type) {
		return false;
		//return disallowedTypes.contains(type);
	}
	
}
