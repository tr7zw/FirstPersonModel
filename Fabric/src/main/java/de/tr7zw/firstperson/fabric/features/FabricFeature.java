package de.tr7zw.firstperson.fabric.features;

import de.tr7zw.firstperson.features.AbstractCosmetic;
import de.tr7zw.firstperson.features.AbstractCosmetic.ModelCreator;
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

public class FabricFeature
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
		implements de.tr7zw.firstperson.features.FeatureRenderer {

	private final AbstractCosmetic cosmetic;
	private final PlayerEntityModel<AbstractClientPlayerEntity> parentModel;
	private ModelPart model;

	public FabricFeature(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
			AbstractCosmetic cosmetic) {
		super(context);
		this.parentModel = context.getModel();
		this.cosmetic = cosmetic;
		this.model = (ModelPart) cosmetic.getModel(this).getModel();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
			AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta,
			float animationProgress, float headYaw, float headPitch) {
		if (entity.hasSkinTexture() && !entity.isInvisible() && cosmetic.shouldRender(entity, matrices)) {
			VertexConsumer vertexConsumer = vertexConsumers
					.getBuffer((RenderLayer) cosmetic.getRenderLayer(this, entity));
			int m = LivingEntityRenderer.getOverlay(entity, 0.0F);

			matrices.push();
			switch (cosmetic.getAttachedTo()) {
			case HEAD: {
				parentModel.head.rotate(matrices);
			}
			}

			this.model.render(matrices, vertexConsumer, light, m);
			matrices.pop();
		}
	}

	@Override
	public ModelCreator getVanillaModelCreator(int textureWith, int textureHeight, int u, int v) {
		return getVanillaModelCreator(new ModelPart(textureWith, textureHeight, u, v));
	}
	
	@Override
	public ModelCreator getVanillaModelCreator(int u, int v) {
		return getVanillaModelCreator(new ModelPart(parentModel, u, v));
	}
	
	private ModelCreator getVanillaModelCreator(ModelPart modelPart) {
		return new ModelCreator() {

			@Override
			public ModelCreator setTextureOffset(int u, int v) {
				modelPart.setTextureOffset(u, v);
				return this;
			}

			@Override
			public Object getModel() {
				return modelPart;
			}

			@Override
			public ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra,
					boolean mirror) {
				modelPart.addCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, mirror);
				return this;
			}

			@Override
			public ModelCreator setPivot(float x, float y, float z) {
				modelPart.setPivot(x, y, z);
				return this;
			}
		};
	}

	@Override
	public Object getRenderLayerPlayerTextureCutout(Object player) {
		return RenderLayer.getEntityCutout(((AbstractClientPlayerEntity) player).getSkinTexture());
	}

	@Override
	public Object getRenderLayerEntitySolid(Object texture) {
		return RenderLayer.getEntitySolid((Identifier) texture);
	}

}
