package dev.tr7zw.firstperson.fabric.features.boot;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.tr7zw.firstperson.features.Boots;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class Boots1FeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public Boots1FeatureRenderer(
			RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> featureRendererContext) {
		super(featureRendererContext);
		bb_main = new ModelPart(new ArrayList(), new HashMap());
		cube_r1 = new ModelPart(new ArrayList(), new HashMap());
		/*bb_main = new ModelPart();
		bb_main.setPivot(0.0F, 12.0F, 0.0F);
		
		cube_r1 = new ModelPart(32, 32, 0, 0);
		cube_r1.setPivot(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, false);
		cube_r1.setTextureOffset(0, 10).addCuboid(-4.0F, -2.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		cube_r1.setTextureOffset(0, 16).addCuboid(-3.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		cube_r1.setTextureOffset(12, 12).addCuboid(1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);*/
		
		texture = new ResourceLocation("firstperson", "textures/features/boots/boot_default.png");
	}
	
	private final ModelPart bb_main;
	private final ModelPart cube_r1;
	private final ResourceLocation texture;

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light,
			AbstractClientPlayer abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta,
			float animationProgress, float headYaw, float headPitch) {
		if(!SettingsUtil.hasEnabled(abstractClientPlayerEntity, Boots.BOOT1))return;
		if(!abstractClientPlayerEntity.getItemBySlot(EquipmentSlot.FEET).isEmpty())return;
		if (abstractClientPlayerEntity.isSkinLoaded() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(RenderType.entitySolid(texture));
			int m = LivingEntityRenderer.getOverlayCoords(abstractClientPlayerEntity, 0.0F);

			matrixStack.pushPose();
			renderEars(matrixStack, vertexConsumer, light, m, ((PlayerModel<AbstractClientPlayer>) this.getParentModel()));
			matrixStack.popPose();
		}
	}
	
	public void renderEars(PoseStack matrices, VertexConsumer vertices, int light, int overlay, PlayerModel<AbstractClientPlayer> model) {
		matrices.pushPose();
		model.leftLeg.translateAndRotate(matrices);
		this.bb_main.render(matrices, vertices, light, overlay);
		matrices.popPose();
		matrices.pushPose();
		model.rightLeg.translateAndRotate(matrices);
		this.bb_main.render(matrices, vertices, light, overlay);
		matrices.popPose();
	}
	
	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

}
