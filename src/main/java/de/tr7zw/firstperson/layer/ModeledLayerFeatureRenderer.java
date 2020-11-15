package de.tr7zw.firstperson.layer;

import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ModeledLayerFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public ModeledLayerFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.head = new SolidPixelModelPart(this.getContextModel());
		float pixelsize = 3f;
		for(int u = 0; u < 8; u++) {
			for(int v = 0; v < 8; v++) {
				this.head.setTextureOffset(38 + u, 7 + v);
				this.head.addCustomCuboid(-10.5f + u*pixelsize, -22.0f + v*pixelsize, -11.8f, 1.0f, 1.0f, 1.0f, 1f);
			}
		}

	}

	private final SolidPixelModelPart head;
	
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (!abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderCustomHelmet(matrixStack, vertexConsumer, i, m);
	}
	
	public void renderCustomHelmet(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
	    matrixStack.push();
	    matrixStack.scale(0.33f, 0.33f, 0.33f);
	    matrixStack.scale(1.09f, 1.09f, 1.09f);
	    ((ModelWithHead)this.getContextModel()).getHead().rotate(matrixStack);
		this.head.customRender(matrixStack, vertices, light, overlay);
	    matrixStack.pop();

	}
	
}
