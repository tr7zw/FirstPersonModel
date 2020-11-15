package de.tr7zw.firstperson.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin{

	@Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
	public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> callback) {
		float value = 0;
		switch (pose) {
			case SWIMMING :
			case FALL_FLYING :
			case SPIN_ATTACK : {
				value = 0.4f;
				break;
			}
			case CROUCHING : {
				value = 1.27f;
				break;
			}
			default:
				value = 1.62f;
		}
		if(((PlayerEntity)(Object)this) == MinecraftClient.getInstance().player) {
			value *= ((float)FirstPersonModelMod.config.playerSize / 100f);
		}
		callback.setReturnValue(value);
		return value;
	}

	
}
