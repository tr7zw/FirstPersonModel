package de.tr7zw.firstperson.features.boot;

import de.tr7zw.firstperson.features.Boots;
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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class Boots1FeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public Boots1FeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		bb_main = new ModelPart(32, 32, 0, 0);
		bb_main.setPivot(0.0F, 12.0F, 0.0F);
		
		cube_r1 = new ModelPart(32, 32, 0, 0);
		cube_r1.setPivot(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, false);
		cube_r1.setTextureOffset(0, 10).addCuboid(-4.0F, -2.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		cube_r1.setTextureOffset(0, 16).addCuboid(-3.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		cube_r1.setTextureOffset(12, 12).addCuboid(1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		
		texture = new Identifier("firstperson", "textures/features/boots/boot_default.png");
	}
	
	private final ModelPart bb_main;
	private final ModelPart cube_r1;
	private final Identifier texture;

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta,
			float animationProgress, float headYaw, float headPitch) {
		if(!SettingsUtil.hasEnabled(abstractClientPlayerEntity, Boots.BOOT1))return;
		if(!abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.FEET).isEmpty())return;
		if (abstractClientPlayerEntity.hasSkinTexture() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(RenderLayer.getEntitySolid(texture));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			matrixStack.push();
			renderEars(matrixStack, vertexConsumer, light, m, ((PlayerEntityModel) this.getContextModel()));
			matrixStack.pop();
		}
	}
	
	public void renderEars(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, PlayerEntityModel model) {
		matrices.push();
		model.leftLeg.rotate(matrices);
		this.bb_main.render(matrices, vertices, light, overlay);
		matrices.pop();
		matrices.push();
		model.rightLeg.rotate(matrices);
		this.bb_main.render(matrices, vertices, light, overlay);
		matrices.pop();
	}
	
	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

}
