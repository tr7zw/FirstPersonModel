package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Stops items in the hand from rendering while in first person with vanilla hands
 *
 */
@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
	private void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		if (livingEntity instanceof LocalPlayer && FirstPersonModelCore.isRenderingPlayer
				&& FirstPersonModelCore.instance.showVanillaHands()) {
			ci.cancel();
			return;
		}
	}
	
}
