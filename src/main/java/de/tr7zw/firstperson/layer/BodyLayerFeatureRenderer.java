package de.tr7zw.firstperson.layer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerEntityModelAccessor;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BodyLayerFeatureRenderer 
extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public BodyLayerFeatureRenderer(
		FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
			super(featureRendererContext);
			this.leftLeg = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 0, 48, true, 0);
			this.rightLeg = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 0, 32, true, 0);
			if(((PlayerEntityModelAccessor)featureRendererContext.getModel()).hasThinArms()) {
				this.leftArm = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 3, 12, 4, 48, 48, true, -2);
				this.rightArm = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 3, 12, 4, 40, 32, true, -2);
			} else {
				this.leftArm = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 48, 48, true, -2);
				this.rightArm = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 40, 32, true, -2);
			}
			this.jacket = HeadLayerFeatureRenderer.wrapBox(this.getContextModel(), 8, 12, 4, 16, 32, true, 0);
	}

	private final SolidPixelModelPart leftLeg;
	private final SolidPixelModelPart rightLeg;
	private final SolidPixelModelPart leftArm;
	private final SolidPixelModelPart rightArm;
	private final SolidPixelModelPart jacket;
	private static int entityCounter = 0;

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (!abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		if (!isEnabled(abstractClientPlayerEntity)) {
			return;
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderLayers(abstractClientPlayerEntity, matrixStack, vertexConsumer, i, m);
	}

	public void renderLayers(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
		float pixelScaling = 1.16f; //1.125f
		// Left leg
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG)) {
			matrixStack.push();
			this.leftLeg.customCopyPositionAndRotation(this.getContextModel().leftLeg);
			this.leftLeg.pivotY -= 2.625f;
			matrixStack.scale(pixelScaling, pixelScaling, pixelScaling);
			if(abstractClientPlayerEntity.isSneaking()) {
				matrixStack.translate(0, 0, -0.03f);
			}
			this.leftLeg.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Right leg
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG)) {
			matrixStack.push();
			this.rightLeg.customCopyPositionAndRotation(this.getContextModel().rightLeg);
			this.rightLeg.pivotY -= 2.625f;
			matrixStack.scale(pixelScaling, pixelScaling, pixelScaling);
			if(abstractClientPlayerEntity.isSneaking()) {
				matrixStack.translate(0, 0, -0.03f);
			}
			this.rightLeg.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Left Arm
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE)) {
			matrixStack.push();
			this.leftArm.customCopyPositionAndRotation(this.getContextModel().leftArm);
			this.leftArm.pivotY -= 0.825f;
			this.leftArm.pivotX -= 0.02f;
			matrixStack.scale(pixelScaling, pixelScaling, pixelScaling);
			if(abstractClientPlayerEntity.isSneaking()) {
				matrixStack.translate(0, 0, 0.01f);
			}
			this.leftArm.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Right Arm
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE)) {
			matrixStack.push();
			this.rightArm.customCopyPositionAndRotation(this.getContextModel().rightArm);
			this.rightArm.pivotY -= 0.825f;
			this.rightArm.pivotX += 0.02f;
			matrixStack.scale(pixelScaling, pixelScaling, pixelScaling);
			if(abstractClientPlayerEntity.isSneaking()) {
				matrixStack.translate(0, 0, 0.01f);
			}
			this.rightArm.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// jacket
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.JACKET)) {
			matrixStack.push();
			this.jacket.customCopyPositionAndRotation(this.getContextModel().jacket);
			this.jacket.pivotY -= 1f;
			matrixStack.scale(pixelScaling, pixelScaling, pixelScaling);
			if(abstractClientPlayerEntity.isSneaking()) {
				matrixStack.translate(0, 0, -0.025f);
			}
			this.jacket.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		
	}
	
	public static boolean isEnabled(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		LayerMode mode = FirstPersonModelMod.config.skinLayerMode;
		if (mode == LayerMode.VANILLA2D)
			return false;
		ClientPlayerEntity thePlayer = MinecraftClient.getInstance().player;
		if (thePlayer == abstractClientPlayerEntity) {
			entityCounter = 0;
			return true;
		}
		if(entityCounter > FirstPersonModelMod.config.layerLimiter)return false;
		if (mode != LayerMode.ONLYSELF) {
			int distance = FirstPersonModelMod.config.optimizedLayerDistance
					* FirstPersonModelMod.config.optimizedLayerDistance;
			boolean ret = thePlayer.getPos().squaredDistanceTo(abstractClientPlayerEntity.getPos()) < distance;
			if(ret)entityCounter++;
			return ret;
		}
		return false;
	}

}
