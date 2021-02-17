package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;

//HeldItemFeatureRenderer
@Mixin(HeldItemLayer.class)
public class HeldItemLayerMixin {

	@Inject(at = @At("HEAD"), method = "func_229135_a_", cancellable = true)
	private void renderItem(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType p_229135_3_, HandSide arm,
			MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, CallbackInfo info) {
		if (entity instanceof ClientPlayerEntity && FirstPersonModelCore.isRenderingPlayer
				&& FirstPersonModelMod.config.firstPerson.vanillaHands) {
			info.cancel();
			return;
		}
	}

	
}
