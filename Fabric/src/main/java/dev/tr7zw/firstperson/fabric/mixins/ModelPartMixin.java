package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

}
