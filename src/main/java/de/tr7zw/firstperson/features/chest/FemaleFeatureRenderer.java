package de.tr7zw.firstperson.features.chest;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.render.CustomModelPart;
import net.minecraft.client.MinecraftClient;
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
		
		//Female1
		this.female1 = new CustomModelPart(this.getContextModel());
	    this.female1.setTextureOffset(20, 20);
	    this.female1.addCustomCuboid(-3.0F, 1.0F, -3.0F, 6, 1, 1);
	    this.female1.setTextureOffset(19, 21);
	    this.female1.addCustomCuboid(-4.0F, 2.0F, -3.0F, 8, 3, 1);
	    
	    //Female2
		this.female2 = new CustomModelPart(this.getContextModel());
	    this.female2.setTextureOffset(20, 20);
	    this.female2.addCustomCuboid(-3.0F, 1.0F, -3.0F, 6, 1, 1);
	    this.female2.setTextureOffset(19, 21);
	    this.female2.addCustomCuboid(-4.0F, 2.0F, -3.0F, 8, 3, 1);
	    
	    this.female2.setTextureOffset(20, 21);
	    this.female2.addCustomCuboid(-3.0F, 2.0F, -4.0F, 6, 1, 1);
	    this.female2.setTextureOffset(19, 22);
	    this.female2.addCustomCuboid(-4.0F, 3.0F, -4.0F, 8, 1, 1);
	    this.female2.setTextureOffset(20, 23);
	    this.female2.addCustomCuboid(-3.0F, 4.0F, -4.0F, 2, 1, 1);
	    this.female2.setTextureOffset(24, 23);
	    this.female2.addCustomCuboid(1.0F, 4.0F, -4.0F, 2, 1, 1);

	}

	private final CustomModelPart female1;
	private final CustomModelPart female2;
	
	private boolean isValid(Chest chest) {
		return chest == Chest.FEMALE1 || chest == Chest.FEMALE2;
	}
	
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (!abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		boolean self = abstractClientPlayerEntity == MinecraftClient.getInstance().player;
		Chest chest = Chest.VANILLA;
		if(self) {
			chest = FirstPersonModelMod.config.chest;
		}else {
			chest = ((PlayerSettings)abstractClientPlayerEntity).getChest();
		}
		if(!isValid(chest))return;
		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntitySolid((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderFemale(matrixStack, vertexConsumer, i, m, chest);
	}
	
	public void renderFemale(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, Chest mode) {
		if(mode == Chest.FEMALE1) {
			this.female1.customCopyPositionAndRotation(this.getContextModel().torso);
			this.female1.customRender(matrices, vertices, light, overlay);
		}
		if(mode == Chest.FEMALE2) {
			this.female2.customCopyPositionAndRotation(this.getContextModel().torso);
			this.female2.customRender(matrices, vertices, light, overlay);
		}
	}
	
}
