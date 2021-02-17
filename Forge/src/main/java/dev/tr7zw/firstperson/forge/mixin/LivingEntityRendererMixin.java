package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHeadToggle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ShulkerEntity;

@Mixin(LivingRenderer.class)
public abstract class LivingEntityRendererMixin {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderHead(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			IRenderTypeBuffer vertexConsumerProvider, int i, CallbackInfo info) {
		if (livingEntity instanceof ShulkerEntity)
			return;// No need to mess with
		if (FirstPersonModelMod.isRenderingPlayer) {
			EntityModel<LivingEntity> model = getEntityModel();
			if (!(model instanceof IHasHead)) {
				FirstPersonModelMod.isRenderingPlayer = false;
				info.cancel();
				return;
			}
			((ModelPartBase) ((IHasHead) model).getModelHead()).setHidden();
			if (model instanceof IHeadToggle) {
				((IHeadToggle) model).func_217146_a(false);
			}
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderReturn(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack,
			IRenderTypeBuffer vertexConsumerProvider, int i, CallbackInfo info) {
		EntityModel<LivingEntity> model = getEntityModel();
		if (model instanceof IHasHead) {
			((ModelPartBase) ((IHasHead) model).getModelHead()).showAgain();
			if (model instanceof IHeadToggle) {
				((IHeadToggle) model).func_217146_a(true);
			}
		}
		FirstPersonModelMod.isRenderingPlayer = false;
	}

	@Shadow
	public abstract EntityModel<LivingEntity> getEntityModel();

}
