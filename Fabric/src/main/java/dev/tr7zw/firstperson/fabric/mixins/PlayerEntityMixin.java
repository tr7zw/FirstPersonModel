package dev.tr7zw.firstperson.fabric.mixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.config.CosmeticSettings;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import dev.tr7zw.firstperson.fabric.render.SolidPixelModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

/**
 * Keep player specific settings, data and modifies the eye location when enabled
 *
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private CosmeticSettings settings;
	private SolidPixelModelPart headLayer;
	private SolidPixelModelPart[] skinLayer;

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if (this.getUuidAsString().equals(MinecraftClient.getInstance().getSession().getUuid())) {
			// the local var is not used for the own player
		} else {
			settings = new CosmeticSettings();
			FirstPersonModelCore.syncManager.updateSettings(this);
		}
	}

	@Override
	public SolidPixelModelPart[] getSkinLayers() {
		return skinLayer;
	}
	
	@Override
	public void setupSkinLayers(Object[] box) {
		this.skinLayer = (SolidPixelModelPart[]) box;
	}
	
	@Override
	public SolidPixelModelPart getHeadLayers() {
		return headLayer;
	}
	
	@Override
	public void setupHeadLayers(Object box) {
		this.headLayer = (SolidPixelModelPart) box;
	}

	@Override
	public UUID getUUID() {
		return super.uuid;
	}

	@Override
	public void setCosmeticSettings(CosmeticSettings settings) {
		if(((AbstractClientPlayerEntity)(Object)this).isMainPlayer()) {
			settings.modifyCameraHeight = FirstPersonModelCore.config.cosmetic.modifyCameraHeight; // Not provided by the backend
			FirstPersonModelCore.config.cosmetic = settings;
			return;
		}
		this.settings = settings;
	}

	@Override
	public CosmeticSettings getCosmeticSettings() {
		if(((AbstractClientPlayerEntity)(Object)this).isMainPlayer()) {
			return FirstPersonModelCore.config.cosmetic;
		}
		return settings;
	}

	@SuppressWarnings("resource")
	@Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
	public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions,
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
		if (((PlayerEntity) (Object) this) == MinecraftClient.getInstance().player && FirstPersonModelMod.config.cosmetic.modifyCameraHeight) {
			value *= ((float) FirstPersonModelMod.config.cosmetic.playerSize / 100f);
		}
		callback.setReturnValue(value);
		return value;
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		if(isBlockingFast()) {
			this.bodyYaw = headYaw;
			this.prevBodyYaw = prevHeadYaw;
		}
	}
	
	public boolean isBlockingFast() {
		if (this.isUsingItem() && !this.activeItemStack.isEmpty()) {
			Item item = this.activeItemStack.getItem();
			if (item.getUseAction(this.activeItemStack) != UseAction.BLOCK) {
				return false;
			} else {
				return item.getMaxUseTime(this.activeItemStack) > 0;
			}
		} else {
			return false;
		}
	}

}
