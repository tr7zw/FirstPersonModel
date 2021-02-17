package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends
FeatureRenderer<T, M>{

	public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	private boolean hideShoulders = false;
	
	@Inject(method = "renderArmor", at = @At("HEAD"))
	private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T livingEntity,
			EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo info) {
		if(equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.isRenderingPlayer && FirstPersonModelCore.config.firstPerson.vanillaHands) {
			hideShoulders = true;
		}else {
			hideShoulders = false;
		}
	}
	
	@Inject(method = "renderArmorParts", at = @At("HEAD"))
	private void renderArmorParts(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			ArmorItem armorItem, boolean bl, A bipedEntityModel, boolean bl2, float f, float g, float h,
			String string, CallbackInfo info) {
		if(hideShoulders) {
			bipedEntityModel.leftArm.visible = false;
			bipedEntityModel.rightArm.visible = false;
		}
	}
	
}
