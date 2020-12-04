package de.tr7zw.firstperson.features.hat;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;

public class Deadmau5EarsFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public Deadmau5EarsFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		boolean self = abstractClientPlayerEntity == MinecraftClient.getInstance().player;
		Hat hat = Hat.VANILLA;
		if(self) {
			hat = FirstPersonModelMod.config.hat;
		}else {
			hat = ((PlayerSettings)abstractClientPlayerEntity).getHat();
		}
		if(hat != Hat.DEADMAU5)return;
		if (abstractClientPlayerEntity.hasSkinTexture() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(RenderLayer.getEntitySolid(abstractClientPlayerEntity.getSkinTexture()));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			for (int n = 0; n < 2; ++n) {
				float o = MathHelper.lerp(h, abstractClientPlayerEntity.prevYaw, abstractClientPlayerEntity.yaw)
						- MathHelper.lerp(h, abstractClientPlayerEntity.prevBodyYaw,
								abstractClientPlayerEntity.bodyYaw);
				float p = MathHelper.lerp(h, abstractClientPlayerEntity.prevPitch, abstractClientPlayerEntity.pitch);
				matrixStack.push();
				matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(o));
				matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(p));
				matrixStack.translate((double) (0.375F * (float) (n * 2 - 1)), 0.0D, 0.0D);
				matrixStack.translate(0.0D, -0.375D, 0.0D);
				matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-p));
				matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-o));
				float q = 1.3333334F;
				matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
				((PlayerEntityModel) this.getContextModel()).renderEars(matrixStack, vertexConsumer, i, m);
				matrixStack.pop();
			}

		}
	}
}