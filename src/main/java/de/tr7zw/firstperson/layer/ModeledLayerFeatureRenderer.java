package de.tr7zw.firstperson.layer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.MinecraftClient;
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
		float outsideOffset = 11.8f;
		// Front/back
		for(int u = 0; u < 8; u++) {
			for(int v = 0; v < 8; v++) {
				//front
				this.head.setTextureOffset(38 + u, 7 + v);
				this.head.addCustomCuboid(-outsideOffset + u*pixelsize, -22.0f + v*pixelsize, -outsideOffset, 1.0f, 1.0f, 1.0f, 1f);
				//back
				this.head.setTextureOffset(54 + u, 7 + v);
				this.head.addCustomCuboid(-outsideOffset + u*pixelsize, -22.0f + v*pixelsize, outsideOffset, 1.0f, 1.0f, 1.0f, 1f);
			}
		}
		// sides
		for(int u = 0; u < 8; u++) {
			for(int v = 0; v < 8; v++) {
				// left
				this.head.setTextureOffset(30 +7- u, 7 + v);
				this.head.addCustomCuboid(-outsideOffset, -22.0f + v*pixelsize, -outsideOffset + u*pixelsize, 1.0f, 1.0f, 1.0f, 1f);
				// right
				this.head.setTextureOffset(46 + u, 7 + v);
				this.head.addCustomCuboid(outsideOffset - 1.5f, -22.0f + v*pixelsize, -outsideOffset + (u)*pixelsize, 1.0f, 1.0f, 1.0f, 1f);

			}
		}
		// top/bottom
		for(int u = 0; u < 8; u++) {
			for(int v = 0; v < 8; v++) {
				//top
				this.head.setTextureOffset(38 + u, 7-v);
				this.head.addCustomCuboid(-outsideOffset + u*pixelsize, -22.0f, -outsideOffset + v*pixelsize, 1.0f, 1.0f, 1.0f, 1f);
				//bottom
				this.head.setTextureOffset(46 + u, 6-v);
				this.head.addCustomCuboid(-outsideOffset + u*pixelsize, -22.0f + 8*pixelsize, -outsideOffset + v*pixelsize, 1.0f, 1.0f, 1.0f, 1f);
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
		if(abstractClientPlayerEntity == MinecraftClient.getInstance().player && FirstPersonModelMod.isFixActive(abstractClientPlayerEntity, matrixStack)) {
			return;
		}
		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderCustomHelmet(matrixStack, vertexConsumer, i, m);
	}
	
	public void renderCustomHelmet(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
	    matrixStack.push();
	    float scale = 1f/3f;
	    matrixStack.scale(scale, scale, scale);
	    matrixStack.scale(1.09f, 1.09f, 1.09f);
	    ((ModelWithHead)this.getContextModel()).getHead().rotate(matrixStack);
		this.head.customRender(matrixStack, vertices, light, overlay);
	    matrixStack.pop();

	}
	
}
