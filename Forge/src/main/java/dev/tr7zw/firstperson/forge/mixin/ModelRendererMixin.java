package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.renderer.model.ModelRenderer;

@Mixin(ModelRenderer.class)
public class ModelRendererMixin implements ModelPartBase {

	@Shadow
	public float rotationPointZ;

	private float zCopy = 0;
	private boolean moved = false;

	@Override
	public void setHidden() {
		if (!moved)
			zCopy = rotationPointZ;
		rotationPointZ = 5000;
		moved = true;
	}

	@Override
	public void showAgain() {
		if (moved) {
			rotationPointZ = zCopy;
			moved = false;
		}
	}

	@Inject(method = "setRotationPoint", at = @At("RETURN"))
	public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn, CallbackInfo info) {
		if (moved) {
			zCopy = rotationPointZIn;
			this.rotationPointZ = 5000;
		}
	}

	@Inject(method = "copyModelAngles", at = @At("RETURN"))
	public void copyModelAngles(ModelRenderer modelRendererIn, CallbackInfo info) {
		if (moved) {
			zCopy = rotationPointZ;
			this.rotationPointZ = 5000;
		}
	}

	@Override
	public boolean isHidden() {
		return moved;
	}

}
