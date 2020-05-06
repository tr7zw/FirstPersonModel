package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	private int allowBob = 0;

	@Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
	private void bobView(MatrixStack matrixStack_1, float float_1, CallbackInfo info) {
		if (--allowBob == 0) {
			// Allow bob
		} else {
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "renderHand")
	private void renderHand(MatrixStack matrixStack, Camera camera, float f, CallbackInfo info) {
		allowBob = 3;
	}

}
