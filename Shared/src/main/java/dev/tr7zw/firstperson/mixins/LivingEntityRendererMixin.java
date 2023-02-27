package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderHead(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
			MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
		if(livingEntity instanceof Shulker)return;//No need to mess with
		// only run when the player is rendering, and it's not a "Humanoid" model(it otherwise gets handeled in HumanoidModelMixin)
		if (FirstPersonModelCore.isRenderingPlayer && !(getModel() instanceof HumanoidModel)) {
			EntityModel<LivingEntity> model = getModel();
			if(!(model instanceof HeadedModel)) {
				FirstPersonModelCore.isRenderingPlayer = false;
				info.cancel();
				return;
			}
			((ModelPartBase) (Object)((HeadedModel) model).getHead()).setHidden();
			if (model instanceof VillagerHeadModel) {
				((VillagerHeadModel) model).hatVisible(false);
			}
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void renderReturn(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
			MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
		if (FirstPersonModelCore.isRenderingPlayer) {
			EntityModel<LivingEntity> model = getModel();
			if (model instanceof HeadedModel) {
				((ModelPartBase) (Object)((HeadedModel) model).getHead()).showAgain();
				if (model instanceof VillagerHeadModel) {
					((VillagerHeadModel) model).hatVisible(true);
				}
			}
		}
		FirstPersonModelCore.isRenderingPlayer = false;
	}

	@Shadow
	public abstract EntityModel<LivingEntity> getModel();

}
