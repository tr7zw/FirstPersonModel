package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.model.ModelPart;

@Mixin(ModelPart.class)
public class ModelPartMixin implements ModelPartBase{

	@Shadow
	public float pivotZ;
	
	private float zCopy = 0;
	private boolean moved = false;
	
	@Override
	public void setHidden() {
		if(!moved)
			zCopy = pivotZ;
		pivotZ = 5000;
		moved = true;
	}

	@Override
	public void showAgain() {
		if(moved) {
			pivotZ = zCopy;
			moved = false;
		}
	}
	
	@Inject(method = "setPivot", at = @At("RETURN"))
	public void setPivot(float x, float y, float z, CallbackInfo info) {
		if(moved) {
			zCopy = z;
			this.pivotZ = 5000;
		}
	}
	
	@Inject(method = "copyPositionAndRotation", at = @At("RETURN"))
	public void copyPositionAndRotation(ModelPart modelPart, CallbackInfo info) {
		if(moved) {
			zCopy = pivotZ;
			this.pivotZ = 5000;
		}
	}

}
