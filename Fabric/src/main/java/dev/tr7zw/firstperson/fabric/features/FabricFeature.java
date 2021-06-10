package dev.tr7zw.firstperson.fabric.features;

import java.util.ArrayList;
import java.util.HashMap;

import dev.tr7zw.firstperson.fabric.render.CustomModelPart;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.AbstractCosmetic.ModelCreator;
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
		implements dev.tr7zw.firstperson.features.FeatureRenderer {

	private final AbstractCosmetic cosmetic;
	private final PlayerEntityModel<AbstractClientPlayerEntity> parentModel;

	public FabricFeature(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
			AbstractCosmetic cosmetic) {
		super(context);
		this.parentModel = context.getModel();
		this.cosmetic = cosmetic;
		cosmetic.init(this);
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
					break;
				}
				case BODY: {
					parentModel.body.rotate(matrices);
					break;
				}
			}
			cosmetic.updateModel(entity);
			((ModelPart) cosmetic.getModel().getModel()).render(matrices, vertexConsumer, light, m);
			matrices.pop();
		}
	}

	@Override
	public ModelCreator getVanillaModelCreator(int textureWith, int textureHeight, int u, int v) {
		return getVanillaModelCreator(new ModelPart(new ArrayList(), new HashMap()));
	}

	@Override
	public ModelCreator getVanillaModelCreator(int u, int v) {
		return getVanillaModelCreator(new ModelPart(new ArrayList(), new HashMap()));
	}

	private ModelCreator getVanillaModelCreator(ModelPart modelPart) {
		return new ModelCreator() {

			@Override
			public ModelCreator setTextureOffset(int u, int v) {
				//modelPart.setTextureOffset(u, v);
				return this;
			}

			@Override
			public Object getModel() {
				return modelPart;
			}

			@Override
			public ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra,
					boolean mirror) {
				//modelPart.addCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, mirror);
				return this;
			}

			@Override
			public ModelCreator setPivot(float x, float y, float z) {
				modelPart.setPivot(x, y, z);
				return this;
			}

			@Override
			public void addChild(ModelCreator child) {
				//modelPart.addChild((ModelPart) child.getModel());
			}

			@Override
			public void setRotationAngle(float x, float y, float z) {
				modelPart.pitch = x;
				modelPart.yaw = y;
				modelPart.roll = z;
			}
		};
	}

	@Override
	public Object getRenderLayerEntityCutout(Object texture) {
		if (texture instanceof AbstractClientPlayerEntity) {
			return RenderLayer.getEntityCutout(((AbstractClientPlayerEntity) texture).getSkinTexture());
		} else {
			return RenderLayer.getEntityCutout((Identifier) texture);
		}
	}

	@Override
	public Object getRenderLayerEntitySolid(Object texture) {
		if (texture instanceof AbstractClientPlayerEntity) {
			return RenderLayer.getEntitySolid(((AbstractClientPlayerEntity) texture).getSkinTexture());
		} else {
			return RenderLayer.getEntitySolid((Identifier) texture);
		}
	}

	@Override
	public Object getRenderLayerEntityTranslucentCull(Object texture) {
		if (texture instanceof AbstractClientPlayerEntity) {
			return RenderLayer.getEntityTranslucentCull(((AbstractClientPlayerEntity) texture).getSkinTexture());
		} else {
			return RenderLayer.getEntityTranslucentCull((Identifier) texture);
		}
	}

	@Override
	public ModelCreator getCustomModelCreator(int u, int v) {
		return new ModelCreator() {
			CustomModelPart modelPart = new CustomModelPart(parentModel);

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
				modelPart.addCustomCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, mirror);
				return this;
			}

			@Override
			public ModelCreator setPivot(float x, float y, float z) {
				modelPart.setPivot(x, y, z);
				return this;
			}

			@Override
			public void addChild(ModelCreator child) {
				modelPart.addChild((CustomModelPart) child.getModel());
			}

			@Override
			public void setRotationAngle(float x, float y, float z) {
				modelPart.pitch = x;
				modelPart.yaw = y;
				modelPart.roll = z;
			}
		};
	}

}
