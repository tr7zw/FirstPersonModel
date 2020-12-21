package dev.tr7zw.firstperson.forge.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.config.CosmeticSettings;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);

	}

	private CosmeticSettings settings;
	
	
	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if (this.getCachedUniqueIdString().equals(Minecraft.getInstance().getSession().getPlayerID())) {
			// the local var is not used for the own player
		} else {
			settings = new CosmeticSettings();
			FirstPersonModelCore.syncManager.updateSettings(this);
		}
	}
	

	@Override
	public Object[] getSkinLayers() {
		return null;
	}
	
	@Override
	public void setupSkinLayers(Object[] box) {
		
	}
	
	@Override
	public Object getHeadLayers() {
		return null;
	}
	
	@Override
	public void setupHeadLayers(Object box) {
		
	}

	@Override
	public UUID getUUID() {
		return super.entityUniqueID;
	}

	@Override
	public void setCosmeticSettings(CosmeticSettings settings) {
		if(((AbstractClientPlayerEntity)(Object)this).isUser()) {
			settings.modifyCameraHeight = FirstPersonModelCore.config.cosmetic.modifyCameraHeight; // Not provided by the backend
			FirstPersonModelCore.config.cosmetic = settings;
			return;
		}
		this.settings = settings;
	}

	@Override
	public CosmeticSettings getCosmeticSettings() {
		if(((AbstractClientPlayerEntity)(Object)this).isUser()) {
			return FirstPersonModelCore.config.cosmetic;
		}
		return settings;
	}

	@SuppressWarnings("resource")
	@Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
	public float getStandingEyeHeight(Pose pose, EntitySize sizeIn,
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
		if (((PlayerEntity) (Object) this) == Minecraft.getInstance().player && FirstPersonModelMod.config.cosmetic.modifyCameraHeight) {
			value *= ((float) FirstPersonModelMod.config.cosmetic.playerSize / 100f);
		}
		callback.setReturnValue(value);
		return value;
	}
	
	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		if(isBlockingFast()) {
			this.renderYawOffset = rotationYawHead;
			this.prevRenderYawOffset = prevRotationYawHead;
		}
	}
	
	public boolean isBlockingFast() {
		if (this.isHandActive() && !this.activeItemStack.isEmpty()) {
			Item item = this.activeItemStack.getItem();
			if (item.getUseAction(this.activeItemStack) != UseAction.BLOCK) {
				return false;
			} else {
				return item.getUseDuration(this.activeItemStack) > 0;
			}
		} else {
			return false;
		}
	}
	
}
