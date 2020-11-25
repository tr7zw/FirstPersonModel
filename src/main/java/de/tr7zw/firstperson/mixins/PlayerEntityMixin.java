package de.tr7zw.firstperson.mixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerSettings {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private int customHeight = 100;
	private Chest chest = Chest.VANILLA;
	private SolidPixelModelPart headLayer;
	private SolidPixelModelPart[] skinLayer;

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		if (this.getUuidAsString().equals(MinecraftClient.getInstance().getSession().getUuid())) {
			// This is us, don't request it
		} else {
			FirstPersonModelMod.syncManager.updateSettings(this);
		}
	}

	@Override
	public SolidPixelModelPart[] getSkinLayers() {
		return skinLayer;
	}
	
	@Override
	public void setupSkinLayers(SolidPixelModelPart[] box) {
		this.skinLayer = box;
	}
	
	@Override
	public SolidPixelModelPart getHeadLayers() {
		return headLayer;
	}
	
	@Override
	public void setupHeadLayers(SolidPixelModelPart box) {
		this.headLayer = box;
	}
	
	@Override
	public int getCustomHeight() {
		return customHeight;
	}

	@Override
	public Chest getChest() {
		return chest;
	}
	
	@Override
	public void setChest(Chest chest) {
		this.chest = chest;
	}

	@Override
	public UUID getUUID() {
		return super.uuid;
	}

	@Override
	public void setCustomHeight(int customHeight) {
		this.customHeight = customHeight;
	}

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
		if (((PlayerEntity) (Object) this) == MinecraftClient.getInstance().player) {
			value *= ((float) FirstPersonModelMod.config.playerSize / 100f);
		}
		callback.setReturnValue(value);
		return value;
	}

}
