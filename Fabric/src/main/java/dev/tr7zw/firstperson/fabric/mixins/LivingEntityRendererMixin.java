package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderHead(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if(livingEntity instanceof ShulkerEntity)return;//No need to mess with
		if (FirstPersonModelCore.isRenderingPlayer) {
			EntityModel<LivingEntity> model = getModel();
			if(!(model instanceof ModelWithHead)) {
				FirstPersonModelCore.isRenderingPlayer = false;
				info.cancel();
				return;
			}
			((ModelPartBase) ((ModelWithHead) model).getHead()).setHidden();
			if (model instanceof ModelWithHat) {
				((ModelWithHat) model).setHatVisible(false);
			}
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderReturn(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if (FirstPersonModelCore.isRenderingPlayer) {
			EntityModel<LivingEntity> model = getModel();
			if (model instanceof ModelWithHead) {
				((ModelPartBase) ((ModelWithHead) model).getHead()).showAgain();
				if (model instanceof ModelWithHat) {
					((ModelWithHat) model).setHatVisible(true);
				}
			}
			FirstPersonModelCore.isRenderingPlayer = false;
		}
	}

	@Shadow
	public abstract EntityModel<LivingEntity> getModel();

}
