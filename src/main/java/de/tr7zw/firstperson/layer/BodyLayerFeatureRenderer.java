package de.tr7zw.firstperson.layer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
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
			this.leftLeg = ModeledLayerFeatureRenderer.wrapBox(this.getContextModel(), 4, 12, 4, 0, 48);
	
	}

	private final SolidPixelModelPart leftLeg;

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (!abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		if (FirstPersonModelMod.isFixActive(abstractClientPlayerEntity, matrixStack)
				/*|| !isEnabled(abstractClientPlayerEntity)*/) {
			return;
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderLayers(matrixStack, vertexConsumer, i, m);
	}

	public void renderLayers(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
		matrixStack.push();
		this.leftLeg.customCopyPositionAndRotation(this.getContextModel().leftLeg);
		matrixStack.scale(1.18f, 1.18f, 1.18f);
		this.leftLeg.customRender(matrixStack, vertices, light, overlay);
		matrixStack.pop();

	}

}
