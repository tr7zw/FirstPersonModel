package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

//HeldItemFeatureRenderer
@Mixin(HeldItemLayer.class)
public class HeldItemLayerMixin {

	@Inject(at = @At("HEAD"), method = "func_229135_a_", cancellable = true)
	private void renderItem(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType p_229135_3_, HandSide arm,
			MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, CallbackInfo info) {
		if (entity instanceof ClientPlayerEntity && FirstPersonModelCore.instance.isFixActive(entity, matrices)
				&& FirstPersonModelMod.config.firstPerson.vanillaHands) {
			info.cancel();
			return;
		}
		if(entity instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;			
			ArmPose armPose = getArmPose(player, Hand.MAIN_HAND);
			ArmPose armPose2 = getArmPose(player, Hand.OFF_HAND);
			if(!(isUsingboothHands(armPose) || isUsingboothHands(armPose2)))return;
			if (armPose.func_241657_a_()) {
				armPose2 = player.getHeldItemOffhand().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
			}

			if (player.getPrimaryHand() == HandSide.RIGHT) {
				if(arm == HandSide.RIGHT && isUsingboothHands(armPose2)) {
					info.cancel();
					return;
				}else if(arm == HandSide.LEFT && isUsingboothHands(armPose)) {
					info.cancel();
					return;
				}
			} else {
				if(arm == HandSide.LEFT && isUsingboothHands(armPose2)) {
					info.cancel();
					return;
				}else if(arm == HandSide.RIGHT && isUsingboothHands(armPose)) {
					info.cancel();
					return;
				}
			}
		}
	}

	private boolean isUsingboothHands(ArmPose pose) {
		return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
	}

	private static ArmPose getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand) {
		ItemStack itemStack = abstractClientPlayerEntity.getHeldItem(hand);
		if (itemStack.isEmpty()) {
			return ArmPose.EMPTY;
		} else {
			if (abstractClientPlayerEntity.getActiveHand() == hand
					&& abstractClientPlayerEntity.getItemInUseCount() > 0) {
				UseAction useAction = itemStack.getUseAction();
				if (useAction == UseAction.BLOCK) {
					return ArmPose.BLOCK;
				}

				if (useAction == UseAction.BOW) {
					return ArmPose.BOW_AND_ARROW;
				}

				if (useAction == UseAction.SPEAR) {
					return ArmPose.THROW_SPEAR;
				}

				if (useAction == UseAction.CROSSBOW && hand == abstractClientPlayerEntity.getActiveHand()) {
					return ArmPose.CROSSBOW_CHARGE;
				}
			} else if (!abstractClientPlayerEntity.isSwingInProgress && itemStack.getItem() == Items.CROSSBOW
					&& CrossbowItem.isCharged(itemStack)) {
				return ArmPose.CROSSBOW_HOLD;
			}

			return ArmPose.ITEM;
		}
	}
	
}
