package de.tr7zw.mod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRenderMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>{

	public PlayerRenderMixin(EntityRenderDispatcher entityRenderDispatcher_1,
			PlayerEntityModel<AbstractClientPlayerEntity> entityModel_1, float float_1) {
		super(entityRenderDispatcher_1, entityModel_1, float_1);
		// TODO Auto-generated constructor stub
	}

	@Inject(at = @At("RETURN"), method = "setModelPose")
	private void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity_1, CallbackInfo info) {
		if(MinecraftClient.getInstance().options.perspective != 0)return;
		PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel_1 = (PlayerEntityModel)this.getModel();
		playerEntityModel_1.head.visible = false;
		playerEntityModel_1.headwear.visible = false;
	} 

	@Shadow
	abstract void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity_1);

	@Overwrite
	public void method_4215(AbstractClientPlayerEntity abstractClientPlayerEntity_1, double x, double y, double z, float float_1, float float_2) {
		if (!abstractClientPlayerEntity_1.isMainPlayer() || this.renderManager.camera != null && this.renderManager.camera.getFocusedEntity() == abstractClientPlayerEntity_1) {
			double double_4 = y;
			if (abstractClientPlayerEntity_1.isInSneakingPose()) {
				double_4 = y - 0.125D;
			}

			float bodyOffset;
			if(abstractClientPlayerEntity_1 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.perspective == 0) {
				if(abstractClientPlayerEntity_1.isSneaking()){
					bodyOffset = 0.30f;
				}else{
					bodyOffset = 0.22f;
				}
				//(player, player.posX - entity.posX + x + CameraTest.bodyOffset * Math.sin(Math.toRadians(renderOffset)), player.posY - entity.posY + y, player.posZ - entity.posZ + z - CameraTest.bodyOffset * Math.cos(Math.toRadians(renderOffset)), (float)renderOffset, ticks);
				x += bodyOffset * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevYaw));
				z -= bodyOffset * Math.cos(Math.toRadians(abstractClientPlayerEntity_1.prevYaw));
			}

			this.setModelPose(abstractClientPlayerEntity_1);
			GlStateManager.setProfile(GlStateManager.RenderMode.PLAYER_SKIN);
			super.method_4054(abstractClientPlayerEntity_1, x, double_4, z, float_1, float_2);
			GlStateManager.unsetProfile(GlStateManager.RenderMode.PLAYER_SKIN);
		}

	}


}
