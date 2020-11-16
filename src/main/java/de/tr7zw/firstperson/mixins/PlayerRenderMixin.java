package de.tr7zw.firstperson.mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRenderMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>{

	/**
	 * Just needed because of the extends
	 * 
	 * @param entityRenderDispatcher_1
	 * @param entityModel_1
	 * @param float_1
	 */
	public PlayerRenderMixin(EntityRenderDispatcher entityRenderDispatcher_1,
			PlayerEntityModel<AbstractClientPlayerEntity> entityModel_1, float float_1) {
		super(entityRenderDispatcher_1, entityModel_1, float_1);
	}


	@Redirect(method = "render", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"
	))
	private void setModelPoseRedirect(PlayerEntityRenderer playerEntityRenderer, AbstractClientPlayerEntity abstractClientPlayerEntity, AbstractClientPlayerEntity abstractClientPlayerEntity_1, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		setModelPose(abstractClientPlayerEntity);
		if(FirstPersonModelMod.isFixActive(abstractClientPlayerEntity, matrixStack)) {
			this.setModelPose(abstractClientPlayerEntity);
			PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel_1 = (PlayerEntityModel)this.getModel();
			playerEntityModel_1.head.visible = false;
			playerEntityModel_1.helmet.visible = false;
			if(FirstPersonModelMod.config.vanillaHands) {
				playerEntityModel_1.leftArm.visible = false;
				playerEntityModel_1.leftSleeve.visible = false;
				playerEntityModel_1.rightArm.visible = false;
				playerEntityModel_1.rightSleeve.visible = false;
			}
		}
		playerEntityRenderer.getModel().helmet.visible = false;
	} 

	@Shadow
	abstract void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity_1);



	//@Inject(at = @At("HEAD"), method = "getPositionOffset", cancellable = true)


}
