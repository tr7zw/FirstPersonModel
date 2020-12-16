package de.tr7zw.firstperson.fabric.features.hat;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.fabric.FirstPersonModelMod;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.util.SettingsUtil;
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
import net.minecraft.util.Identifier;

public class PlungerFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlungerFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.bone = new ModelPart(32, 32, 0, 0);
		bone.setPivot(0.0F, -8.0F, -1.0F);
		bone.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -2.0F, 5.0F, 3.0F, 5.0F, 0.0F, false);
		bone.setTextureOffset(0, 9).addCuboid(-1.0F, -16.0F, 0.0F, 1.0F, 13.0F, 1.0F, 0.0F, false);
		
		texture = new Identifier("firstperson", "textures/features/hat/plunger.png");
	}
	
	private final ModelPart bone;
	private final Identifier texture;

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if(!SettingsUtil.hasEnabled(abstractClientPlayerEntity, Hat.PLUNGER))return;
		if (FirstPersonModelCore.instance.isFixActive(abstractClientPlayerEntity, matrixStack)) {
			return;
		}
		
		if (abstractClientPlayerEntity.hasSkinTexture() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(RenderLayer.getEntitySolid(texture));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			matrixStack.push();
			renderEars(matrixStack, vertexConsumer, i, m, ((PlayerEntityModel<AbstractClientPlayerEntity>) this.getContextModel()));
			matrixStack.pop();
		}
	}
	
	public void renderEars(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, PlayerEntityModel<AbstractClientPlayerEntity> model) {
		model.head.rotate(matrices);
		this.bone.render(matrices, vertices, light, overlay);
	}
	
}