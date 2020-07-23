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
		if(FirstPersonModelMod.hideHeadWithMatrixStack == matrixStack) {
			this.setModelPose(abstractClientPlayerEntity);
			PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel_1 = (PlayerEntityModel)this.getModel();
			playerEntityModel_1.head.visible = false;
			playerEntityModel_1.helmet.visible = false;
		}
	} 

	@Shadow
	abstract void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity_1);

	private AbstractClientPlayerEntity abstractClientPlayerEntity_1;
	private double realYaw;

	@Inject(at = @At("HEAD"), method = "getPositionOffset", cancellable = true)
	public void getPositionOffset(AbstractClientPlayerEntity var1, float var2, CallbackInfoReturnable<Vec3d> info) {
		if(MinecraftClient.getInstance().player.isSleeping() || FirstPersonModelMod.fixBodyShadow())return;
		double x,y,z = x = y = z = 0;
		if(var1 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.perspective == 0 && FirstPersonModelMod.isRenderingPlayer) {
			this.abstractClientPlayerEntity_1 = (AbstractClientPlayerEntity) var1;
			realYaw = MathHelper.lerpAngleDegrees(MinecraftClient.getInstance().getTickDelta(), abstractClientPlayerEntity_1.prevYaw, abstractClientPlayerEntity_1.yaw);
			 FirstPersonModelMod.isRenderingPlayer = false;
		}else {
			this.abstractClientPlayerEntity_1 = null;
			return;
		}
		if (abstractClientPlayerEntity_1 != null && (!abstractClientPlayerEntity_1.isMainPlayer() || this.getRenderManager().camera != null && this.getRenderManager().camera.getFocusedEntity() == abstractClientPlayerEntity_1)) {
			float bodyOffset;
			if(abstractClientPlayerEntity_1.isSneaking()){
				bodyOffset = FirstPersonModelMod.sneakBodyOffset + (FirstPersonModelMod.config.sneakXOffset / 100f);
			}else if(MinecraftClient.getInstance().player.isInSwimmingPose()) {
				abstractClientPlayerEntity_1.bodyYaw = abstractClientPlayerEntity_1.headYaw;
				if(abstractClientPlayerEntity_1.prevPitch > 0) {
					bodyOffset = FirstPersonModelMod.swimUpBodyOffset;
				}else {
					bodyOffset = FirstPersonModelMod.swimDownBodyOffset;
				}
			}else if(abstractClientPlayerEntity_1.hasVehicle()) {
				bodyOffset = FirstPersonModelMod.inVehicleBodyOffset;
			}else{
				bodyOffset = 0.25f + (FirstPersonModelMod.config.xOffset / 100f);
			}
			x += bodyOffset * Math.sin(Math.toRadians(realYaw));
			z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
			if(MinecraftClient.getInstance().player.isInSwimmingPose()) {
				if(abstractClientPlayerEntity_1.prevPitch > 0  && abstractClientPlayerEntity_1.isSubmergedInWater()) {
					y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}else {
					y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}
			}
			
		}
		Vec3d vec = new Vec3d(x, y, z);
		abstractClientPlayerEntity_1 = null;
		FirstPersonModelMod.isRenderingPlayer = false;
		info.setReturnValue(vec);
	}

}
