package de.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.fabric.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;

/**
 * Temporary fix for the view bobbing and map rendering. 
 *
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	private final MinecraftClient mc = MinecraftClient.getInstance();
	
	// Bobbing needs to be disabled for the minimap to work(and not bob), but everywhere else it should work
	// This does the job in vanilla but there is probably a better way (probably fixing the minimap^^)
	private int allowBob = 0;

	@Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
	private void bobView(MatrixStack matrixStack_1, float float_1, CallbackInfo info) {
		if (--allowBob == 0 || !hasMapInHand()) {
			// Allow bob
		} else {
		if(FirstPersonModelMod.enabled)
			info.cancel();
		}
	}
	
	private boolean hasMapInHand() {
		return mc.player.getMainHandStack().getItem() == Items.FILLED_MAP ||
				mc.player.getOffHandStack().getItem() == Items.FILLED_MAP;
	}

	@Inject(at = @At("HEAD"), method = "renderHand")
	private void renderHand(MatrixStack matrixStack, Camera camera, float f, CallbackInfo info) {
		allowBob = 3;
	}

}
