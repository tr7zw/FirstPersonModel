package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;

/**
 * Stops items in the hand from rendering while in first person.
 *
 */
@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderItem", cancellable = true)
	private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		if (entity instanceof ClientPlayerEntity && FirstPersonModelMod.isFixActive(entity, matrices)
				&& FirstPersonModelMod.config.firstPerson.vanillaHands) {
			info.cancel();
			return;
		}
		if(entity instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;
			ArmPose armPose = getArmPose(player, Hand.MAIN_HAND);
			ArmPose armPose2 = getArmPose(player, Hand.OFF_HAND);
			if(!(armPose == ArmPose.BOW_AND_ARROW || armPose2 == ArmPose.BOW_AND_ARROW))return;
			if (armPose.method_30156()) {
				armPose2 = player.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
			}

			if (player.getMainArm() == Arm.RIGHT) {
				if(arm == Arm.RIGHT && armPose2 == ArmPose.BOW_AND_ARROW) {
					info.cancel();
					return;
				}else if(arm == Arm.LEFT && armPose == ArmPose.BOW_AND_ARROW) {
					info.cancel();
					return;
				}
			} else {
				if(arm == Arm.LEFT && armPose2 == ArmPose.BOW_AND_ARROW) {
					info.cancel();
					return;
				}else if(arm == Arm.RIGHT && armPose == ArmPose.BOW_AND_ARROW) {
					info.cancel();
					return;
				}
			}
		}
	}

	private static ArmPose getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand) {
		ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
		if (itemStack.isEmpty()) {
			return ArmPose.EMPTY;
		} else {
			if (abstractClientPlayerEntity.getActiveHand() == hand
					&& abstractClientPlayerEntity.getItemUseTimeLeft() > 0) {
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
			} else if (!abstractClientPlayerEntity.handSwinging && itemStack.getItem() == Items.CROSSBOW
					&& CrossbowItem.isCharged(itemStack)) {
				return ArmPose.CROSSBOW_HOLD;
			}

			return ArmPose.ITEM;
		}
	}
	
}
