package de.tr7zw.firstperson.features.hat;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Hat;
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

public class Deadmau5EarsFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public Deadmau5EarsFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.ears = new ModelPart(featureRendererContext.getModel(), 25, 1);
		this.ears.addCuboid(3.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1); // left
		this.ears.setTextureOffset(25, 1);
		this.ears.addCuboid(-9.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1); // right
	}
	
	private final ModelPart ears;

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
		if (FirstPersonModelMod.isFixActive(abstractClientPlayerEntity, matrixStack)) {
			return;
		}
		
		if (abstractClientPlayerEntity.hasSkinTexture() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(RenderLayer.getEntityCutout(abstractClientPlayerEntity.getSkinTexture()));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			matrixStack.push();
			renderEars(matrixStack, vertexConsumer, i, m, ((PlayerEntityModel) this.getContextModel()));
			matrixStack.pop();
		}
	}
	
	public void renderEars(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, PlayerEntityModel model) {
		model.head.rotate(matrices);
		this.ears.pivotX = 0.0F;
		this.ears.pivotY = 0.0F;
		this.ears.render(matrices, vertices, light, overlay);
	}
	
}