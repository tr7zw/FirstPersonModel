package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Hides body parts and layers where needed
 *
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRenderMixin
		extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	
    
	/**
	 * Just needed because of the extends
	 * 
	 * @param ctx
	 * @param model
	 * @param shadowRadius
	 */
	public PlayerRenderMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    } 
    
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"))
	private void setModelPoseRedirect(PlayerEntityRenderer playerEntityRenderer,
			AbstractClientPlayerEntity abstractClientPlayerEntity,
			AbstractClientPlayerEntity abstractClientPlayerEntity_1, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i) {
		setModelPose(abstractClientPlayerEntity);
		if (FirstPersonModelMod.isRenderingPlayer) {
			PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel_1 = this.getModel();
			playerEntityModel_1.head.visible = false;
			playerEntityModel_1.hat.visible = false;
			((ModelPartBase)playerEntityModel_1.head).setHidden();
			if (FirstPersonModelMod.config.firstPerson.vanillaHands) {
				playerEntityModel_1.leftArm.visible = false;
				playerEntityModel_1.leftSleeve.visible = false;
				playerEntityModel_1.rightArm.visible = false;
				playerEntityModel_1.rightSleeve.visible = false;
			} else {
				
			}
		} else {
			playerEntityRenderer.getModel().hat.visible = playerEntityRenderer.getModel().hat.visible;
		}
		playerEntityRenderer.getModel().leftSleeve.visible = playerEntityRenderer.getModel().leftSleeve.visible;
		playerEntityRenderer.getModel().rightSleeve.visible = playerEntityRenderer.getModel().rightSleeve.visible;
		playerEntityRenderer.getModel().leftPants.visible = playerEntityRenderer.getModel().leftPants.visible;
		playerEntityRenderer.getModel().rightPants.visible = playerEntityRenderer.getModel().rightPants.visible;
		playerEntityRenderer.getModel().jacket.visible = playerEntityRenderer.getModel().jacket.visible;
	}
	
	@Inject(method = "render", at = @At(value = "RETURN"))
	public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		((ModelPartBase)this.getModel().head).showAgain();
		if (FirstPersonModelMod.isRenderingPlayer) {
			FirstPersonModelMod.isRenderingPlayer = false;
		}
	}

	@Shadow
	abstract void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity_1);

	// @Inject(at = @At("HEAD"), method = "getPositionOffset", cancellable = true)

}
