package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	
	@Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
	   private void bobView(MatrixStack matrixStack_1, float float_1, CallbackInfo info) {
		   info.cancel();
	   }
	
}
