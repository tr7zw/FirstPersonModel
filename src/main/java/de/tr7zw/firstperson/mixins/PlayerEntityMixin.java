package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.PlayerSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings{

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private int customHeight = 100;
	private boolean femaleModel = false;
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if(this.getDisplayName().asString().contains("tr7zw")) { // Custom loading here
			customHeight = 110;
			femaleModel = false;
		}
		if(this.getUuidAsString().equals("3d8930c8-c51f-49b8-b78d-48fdcb2139f9")) { // Custom loading here
			customHeight = 88;
			femaleModel = true;
		}
	}

	@Override
	public int getCustomHeight() {
		return customHeight;
	}

	@Override
	public boolean femaleModel() {
		return femaleModel;
	}

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
