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
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class BodyLayerFeatureRenderer 
extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public BodyLayerFeatureRenderer(
		FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
			super(featureRendererContext);
			this.leftLeg = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 0, 48, true);
			this.rightLeg = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 0, 32, true);
			if(((PlayerEntityModelAccessor)featureRendererContext.getModel()).hasThinArms()) {
				this.leftArm = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 3, 12, 4, 48, 48, true);
				this.rightArm = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 3, 12, 4, 40, 32, true);
			} else {
				this.leftArm = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 48, 48, true);
				this.rightArm = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 40, 32, true);
			}
			this.jacket = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 8, 12, 4, 16, 32, true);
	}

	private final SolidPixelModelPart leftLeg;
	private final SolidPixelModelPart rightLeg;
	private final SolidPixelModelPart leftArm;
	private final SolidPixelModelPart rightArm;
	private final SolidPixelModelPart jacket;

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
		// Left leg
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG)) {
			matrixStack.push();
			this.leftLeg.customCopyPositionAndRotation(this.getContextModel().leftLeg);
			this.leftLeg.pivotY -= 1.5f;
			matrixStack.scale(1.125f, 1.125f, 1.125f);
			this.leftLeg.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Right leg
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG)) {
			matrixStack.push();
			this.rightLeg.customCopyPositionAndRotation(this.getContextModel().rightLeg);
			this.rightLeg.pivotY -= 1.2f;
			matrixStack.scale(1.125f, 1.125f, 1.125f);
			this.rightLeg.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Left Arm
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE)) {
			matrixStack.push();
			this.leftArm.customCopyPositionAndRotation(this.getContextModel().leftArm);
			this.leftArm.pivotY -= 2.5f;
			matrixStack.scale(1.125f, 1.125f, 1.125f);
			this.leftArm.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// Right Arm
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.LEFT_SLEEVE)) {
			matrixStack.push();
			this.rightArm.customCopyPositionAndRotation(this.getContextModel().rightArm);
			this.rightArm.pivotY -= 2.5f;
			this.rightArm.pivotZ += 0.9f;
			matrixStack.scale(1.125f, 1.125f, 1.125f);
			this.rightArm.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		// jacket
		if(abstractClientPlayerEntity.isPartVisible(PlayerModelPart.JACKET)) {
			matrixStack.push();
			this.jacket.customCopyPositionAndRotation(this.getContextModel().jacket);
			this.jacket.pivotY -= 1f;
			matrixStack.scale(1.135f, 1.135f, 1.135f);
			this.jacket.customRender(matrixStack, vertices, light, overlay);
			matrixStack.pop();
		}
		
	}
	
	public static boolean isEnabled(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		LayerMode mode = FirstPersonModelMod.config.skinLayerMode;
		if (mode == LayerMode.DEFAULT)
			return false;
		ClientPlayerEntity thePlayer = MinecraftClient.getInstance().player;
		if (thePlayer == abstractClientPlayerEntity || mode == LayerMode.EVERYONE) {
			return true;
		}
		if (mode != LayerMode.SELF) {
			int distance = FirstPersonModelMod.config.optimizedLayerDistance
					* FirstPersonModelMod.config.optimizedLayerDistance;
			return thePlayer.getPos().squaredDistanceTo(abstractClientPlayerEntity.getPos()) < distance;
		}
		return false;
	}

}
