package de.tr7zw.firstperson;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
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

public class FemaleFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public FemaleFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.female = new ModelPart(this.getContextModel());
	    this.female.setTextureOffset(20, 20);
	    this.female.addCuboid(-3.0F, 1.0F, -3.0F, 6, 1, 1);
	    this.female.setTextureOffset(19, 21);
	    this.female.addCuboid(-4.0F, 2.0F, -3.0F, 8, 3, 1);
	    this.female.setTextureOffset(19, 21);
	    this.female.addCuboid(-4.0F, 2.0F, -3.0F, 8, 3, 1);
	}

	private final ModelPart female;
	
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (abstractClientPlayerEntity != MinecraftClient.getInstance().player || !FirstPersonModelMod.config.femaleModel || !abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntitySolid((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderFemale(matrixStack, vertexConsumer, i, m);
	}
	
	public void renderFemale(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		this.female.copyPositionAndRotation(this.getContextModel().torso);
		this.female.render(matrices, vertices, light, overlay);
	}
	
}
