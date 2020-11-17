package de.tr7zw.firstperson.layer;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModeledLayerFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public ModeledLayerFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.head = wrapBox(this.getContextModel(), 8, 8, 8, 32, 0, false);

	}

	public static SolidPixelModelPart wrapBox(PlayerEntityModel<AbstractClientPlayerEntity> model, int width,
			int height, int depth, int textureU, int textureV, boolean topPivot) {
		SolidPixelModelPart wrapper = new SolidPixelModelPart(model);
		float pixelSize = 1f;
		float staticXOffset = -width/2f;
		float staticYOffset = topPivot ? 0f : -height + 0.6f;//-7.4f;
		float staticZOffset = -depth/2f;
		// Front/back
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				// front
				wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 1 + v);
				wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset, pixelSize, pixelSize, pixelSize);
				// back
				wrapper.setTextureOffset(textureU + 2*depth + width - 2 + u, textureV + depth - 1 + v); // 54 + u, 7 + v
				wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset + depth - 1, pixelSize, pixelSize, pixelSize);
			}
		}
		// sides
		for (int u = 0; u < depth; u++) {
			for (int v = 0; v < height; v++) {
				// left
				wrapper.setTextureOffset(textureU - 3 + depth - u, textureV + depth - 1 + v); // 30 + 7 - u, 7 + v
				wrapper.addCustomCuboid(staticXOffset + 0, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);
				// right
				wrapper.setTextureOffset(textureU -2 + depth + width + u, textureV + depth - 1 + v); // 46 + u
				wrapper.addCustomCuboid(staticXOffset + width - 1f, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);

			}
		}
		// top/bottom
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < depth; v++) {
				// top
				wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 2 - v); // 38 + u
				wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset, staticZOffset + v, pixelSize, pixelSize, pixelSize);
				// bottom
				wrapper.setTextureOffset(textureU + depth + width - 2 + u, textureV + depth - 2 - v); // 46 + u
				wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset + height - 1f, staticZOffset + v, pixelSize, pixelSize, pixelSize);
			}
		}
		return wrapper;
	}

	private final SolidPixelModelPart head;

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k,
			float l) {
		if (!abstractClientPlayerEntity.hasSkinTexture() || abstractClientPlayerEntity.isInvisible()) {
			return;
		}
		ItemStack itemStack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.HEAD);
		if (itemStack != null && ((itemStack.getItem() instanceof BlockItem))) {
			return;
		}
		if (FirstPersonModelMod.isFixActive(abstractClientPlayerEntity, matrixStack)
				|| !isEnabled(abstractClientPlayerEntity)) {
			return;
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderCustomHelmet(matrixStack, vertexConsumer, i, m);
	}

	public void renderCustomHelmet(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
		matrixStack.push();
		this.head.customCopyPositionAndRotation(this.getContextModel().head);
		matrixStack.scale(1.18f, 1.18f, 1.18f);
		this.head.customRender(matrixStack, vertices, light, overlay);
		matrixStack.pop();

	}

	public static boolean isEnabled(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		LayerMode mode = FirstPersonModelMod.config.layerMode;
		if (mode == LayerMode.DEFAULT || !abstractClientPlayerEntity.isPartVisible(PlayerModelPart.HAT))
			return false;
		ClientPlayerEntity thePlayer = MinecraftClient.getInstance().player;
		if (thePlayer == abstractClientPlayerEntity || mode == LayerMode.EVERYONE) {
			return true;
		}
		if (mode != LayerMode.SELF) {
			int distance = FirstPersonModelMod.config.optimizedLayerDistance
					* FirstPersonModelMod.config.optimizedLayerDistance;
			return thePlayer.getPos().squaredDistanceTo(abstractClientPlayerEntity.getPos()) < distance;
		}
		return false;
	}

}
