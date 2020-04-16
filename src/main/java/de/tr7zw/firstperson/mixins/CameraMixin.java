package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow
	boolean thirdPerson;
	@Inject(at = @At("HEAD"), method = "isThirdPerson", cancellable = true)
	public boolean isThirdPerson(CallbackInfoReturnable<Boolean> info) {
		if(MinecraftClient.getInstance().player.isUsingRiptide())return false;
		if(MinecraftClient.getInstance().player.isFallFlying())return false;
		if(MinecraftClient.getInstance().player.isSpectator())return false;
		if(!this.thirdPerson)FirstPersonModelMod.hideNextHeadItem = true;
		info.setReturnValue(true);
		return true;
	}



}