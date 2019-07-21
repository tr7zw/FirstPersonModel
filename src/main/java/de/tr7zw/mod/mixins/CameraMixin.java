package de.tr7zw.mod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public abstract class CameraMixin {
	
	@Inject(at = @At("HEAD"), method = "isThirdPerson", cancellable = true)
	public boolean isThirdPerson(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(true);
		return true;
	}
	

	
}
