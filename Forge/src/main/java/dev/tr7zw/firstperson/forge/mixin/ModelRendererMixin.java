package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.renderer.model.ModelRenderer;

@Mixin(ModelRenderer.class)
public class ModelRendererMixin implements ModelPartBase{

	@Shadow
	public float rotationPointZ;
	
	private float zCopy = 0;
	private boolean moved = false;
	
	@Override
	public void setHidden() {
		zCopy = rotationPointZ;
		rotationPointZ = 5000;
		moved = true;
	}

	@Override
	public void showAgain() {
		if(moved) {
			rotationPointZ = zCopy;
			moved = false;
		}
	}

}

