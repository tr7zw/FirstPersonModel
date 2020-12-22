package dev.tr7zw.firstperson.forge.features;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import de.tr7zw.firstperson.features.AbstractCosmetic;
import de.tr7zw.firstperson.features.AbstractCosmetic.ModelCreator;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ForgeFeature extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
		implements de.tr7zw.firstperson.features.FeatureRenderer {

	private final AbstractCosmetic cosmetic;
	private final PlayerModel<AbstractClientPlayerEntity> parentModel;
	private ModelRenderer model;

	public ForgeFeature(
			IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRendererIn,
			AbstractCosmetic cosmetic) {
		super(entityRendererIn);
		this.cosmetic = cosmetic;
		this.parentModel = entityRendererIn.getEntityModel();
		this.model = (ModelRenderer) cosmetic.getModel(this).getModel();
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
			AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		if (entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible() && cosmetic.shouldRender(entitylivingbaseIn, matrixStackIn)) {
			IVertexBuilder vertexConsumer = bufferIn
					.getBuffer((RenderType) cosmetic.getRenderLayer(this, entitylivingbaseIn));
			int m = LivingRenderer.getPackedOverlay(entitylivingbaseIn, 0.0F);

			matrixStackIn.push();
			switch (cosmetic.getAttachedTo()) {
			case HEAD: {
				parentModel.bipedHead.translateRotate(matrixStackIn);
			}
			}

			this.model.render(matrixStackIn, vertexConsumer, packedLightIn, m);
			matrixStackIn.pop();
		}
	}

	@Override
	public ModelCreator getVanillaModelCreator(int textureWith, int textureHeight, int u, int v) {
		return getVanillaModelCreator(new ModelRenderer(textureWith, textureHeight, u, v));
	}

	
	@Override
	public ModelCreator getVanillaModelCreator(int u, int v) {
		return getVanillaModelCreator(new ModelRenderer(parentModel, u, v));
	}
	
	private ModelCreator getVanillaModelCreator(ModelRenderer modelPart) {
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
				modelPart.addBox(x, y, z, sizeX, sizeY, sizeZ, extra, mirror);
				return this;
			}

			@Override
			public ModelCreator setPivot(float x, float y, float z) {
				modelPart.setRotationPoint(x, y, z);
				return this;
			}
		};
	}

	@Override
	public Object getRenderLayerPlayerTextureCutout(Object player) {
		return RenderType.getEntityCutout(((AbstractClientPlayerEntity)player).getLocationSkin());
	}

	@Override
	public Object getRenderLayerEntitySolid(Object texture) {
		return RenderType.getEntitySolid((ResourceLocation) texture);
	}

}
