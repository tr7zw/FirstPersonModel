package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderHead(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if (FirstPersonModelCore.instance.isCompatebilityMatrix(livingEntity, matrixStack)) {
			EntityModel<LivingEntity> model = getModel();
			if(FirstPersonModelCore.instance.isDisallowedEntityType(livingEntity.getType())) {
				info.cancel();
				return;
			}
			if (model instanceof BipedEntityModel) {
				((ModelPartBase) ((BipedEntityModel) model).head).setHidden();
				((ModelPartBase) ((BipedEntityModel) model).helmet).setHidden();
			}
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderReturn(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if (FirstPersonModelCore.instance.isCompatebilityMatrix(livingEntity, matrixStack)) {
			if (getModel() instanceof BipedEntityModel) {
				((ModelPartBase) ((BipedEntityModel) getModel()).head).showAgain();
				((ModelPartBase) ((BipedEntityModel) getModel()).helmet).showAgain();
				FirstPersonModelMod.clearHeadStack();
			}
		}
	}

	@Shadow
	public abstract EntityModel<LivingEntity> getModel();

}
