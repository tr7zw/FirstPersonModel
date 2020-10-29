package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderItem", cancellable = true)
	private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		if(entity instanceof ClientPlayerEntity && FirstPersonModelMod.isFixActive(entity, matrices) && FirstPersonModelMod.config.vanillaHands) {
			info.cancel();
		}
	}
	
}
