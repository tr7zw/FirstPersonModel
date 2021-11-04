package dev.tr7zw.firstperson.fabric.mixins;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.config.CosmeticSettings;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;

/**
 * Keep player specific settings, data and modifies the eye location when enabled
 *
 */
@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	private CosmeticSettings settings;
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if (this.getStringUUID().equals(Minecraft.getInstance().getUser().getUuid())) {
			// the local var is not used for the own player
		} else {
			settings = new CosmeticSettings();
			FirstPersonModelCore.syncManager.updateSettings(this);
		}
	}

	@Override
	public UUID getUUID() {
		return super.uuid;
	}

	@Override
	public void setCosmeticSettings(CosmeticSettings settings) {
		if(((AbstractClientPlayer)(Object)this).isLocalPlayer()) {
			settings.modifyCameraHeight = FirstPersonModelCore.config.cosmetic.modifyCameraHeight; // Not provided by the backend
			FirstPersonModelCore.config.cosmetic = settings;
			return;
		}
		this.settings = settings;
	}

	@Override
	public CosmeticSettings getCosmeticSettings() {
		if(((AbstractClientPlayer)(Object)this).isLocalPlayer()) {
			return FirstPersonModelCore.config.cosmetic;
		}
		return settings;
	}

	@SuppressWarnings("resource")
	@Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
	public float getActiveEyeHeight(Pose pose, EntityDimensions dimensions,
			CallbackInfoReturnable<Float> callback) {
		float value = 0;
		switch (pose) {
		case SWIMMING:
		case FALL_FLYING:
		case SPIN_ATTACK: {
			value = 0.4f;
			break;
		}
		case CROUCHING: {
			value = 1.27f;
			break;
		}
		default:
			value = 1.62f;
		}
		if (((Player) (Object) this) == Minecraft.getInstance().player && FirstPersonModelMod.config.cosmetic.modifyCameraHeight) {
			value *= ((float) FirstPersonModelMod.config.cosmetic.playerSize / 100f);
		}
		callback.setReturnValue(value);
		return value;
	}
	
}
