package de.tr7zw.firstperson.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Inject(at = @At("HEAD"), method = "isThirdPerson", cancellable = true)
	public void isThirdPerson(CallbackInfoReturnable<Boolean> info) {
		if(MinecraftClient.getInstance().player.isUsingRiptide())return;
		if(MinecraftClient.getInstance().player.isFallFlying())return;
		info.setReturnValue(true);
	}

}
