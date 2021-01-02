package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererMixin {
	@Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
	private static <T extends LivingEntity> void removeHead(EntityModel<T> model, Identifier texture,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green,
			float blue, CallbackInfo ci) {
		if (FirstPersonModelCore.instance.isCompatebilityMatrix(null, matrices)) {
			if (!(model instanceof ModelWithHead)) {
				ci.cancel();
				return;
			}
			((ModelPartBase) ((ModelWithHead) model).getHead()).setHidden();
			if (model instanceof ModelWithHat) {
				((ModelWithHat) model).setHatVisible(false);
			}
		}
	}

	@Inject(method = "renderModel", at = @At("RETURN"), cancellable = true)
	private static <T extends LivingEntity> void removeReturn(EntityModel<T> model, Identifier texture,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green,
			float blue, CallbackInfo ci) {
		if (model instanceof ModelWithHead) {
			((ModelPartBase) ((ModelWithHead) model).getHead()).showAgain();
			if (model instanceof ModelWithHat) {
				((ModelWithHat) model).setHatVisible(true);
			}
		}
	}

}
