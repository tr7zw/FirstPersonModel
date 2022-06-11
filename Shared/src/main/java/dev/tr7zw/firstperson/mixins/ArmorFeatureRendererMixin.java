package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends
RenderLayer<T, M>{

	public ArmorFeatureRendererMixin(RenderLayerParent<T, M> context) {
		super(context);
	}

	private boolean hideShoulders = false;
	private boolean hideHelmet = false;
	
	@Inject(method = "renderArmorPiece", at = @At("HEAD"))
	private void renderArmor(PoseStack matrices, MultiBufferSource vertexConsumers, T livingEntity,
			EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo info) {
		hideShoulders = equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.isRenderingPlayer && FirstPersonModelCore.config.vanillaHands;
		hideHelmet = equipmentSlot == EquipmentSlot.HEAD && FirstPersonModelCore.isRenderingPlayer;
	}
	
	@Inject(method = "renderModel", at = @At("HEAD"))
	private void renderArmorParts(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i,
			ArmorItem armorItem, boolean bl, A bipedEntityModel, boolean bl2, float f, float g, float h,
			String string, CallbackInfo info) {
		if(hideShoulders) {
			bipedEntityModel.leftArm.visible = false;
			bipedEntityModel.rightArm.visible = false;
		}
		if(hideHelmet) {
			bipedEntityModel.head.visible = false;
			bipedEntityModel.hat.visible = false;
		}
	}
	
}
