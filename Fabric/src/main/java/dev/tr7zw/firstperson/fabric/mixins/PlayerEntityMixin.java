package dev.tr7zw.firstperson.fabric.mixins;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.config.CosmeticSettings;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import dev.tr7zw.firstperson.fabric.render.CustomizableModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
	private CustomizableModelPart headLayer;
	private CustomizableModelPart[] skinLayer;
	private static final Set<Item> lockItems = new HashSet<>();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if(lockItems.size() == 0) { // Late bind
			lockItems.add(Items.FILLED_MAP);
			lockItems.add(Items.COMPASS);
		}
		if (this.getUuidAsString().equals(MinecraftClient.getInstance().getSession().getUuid())) {
			// the local var is not used for the own player
		} else {
			settings = new CosmeticSettings();
			FirstPersonModelCore.syncManager.updateSettings(this);
		}
	}

	@Override
	public CustomizableModelPart[] getSkinLayers() {
		return skinLayer;
	}
	
	@Override
	public void setupSkinLayers(Object[] box) {
		this.skinLayer = (CustomizableModelPart[]) box;
	}
	
	@Override
	public CustomizableModelPart getHeadLayers() {
		return headLayer;
	}
	
	@Override
	public void setupHeadLayers(Object box) {
		this.headLayer = (CustomizableModelPart) box;
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
		if(FirstPersonModelCore.config.firstPerson.lockBodyOnItems && (lockItems.contains(getMainHandStack().getItem()) || lockItems.contains(getOffHandStack().getItem()))) {
			this.bodyYaw = headYaw;
			this.prevBodyYaw = prevHeadYaw;
		}
	}
	
}
