package de.tr7zw.firstperson.features.layers;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.LayerMode;
import de.tr7zw.firstperson.render.SolidPixelWrapper;
import de.tr7zw.firstperson.util.SkinUtil;
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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HeadLayerFeatureRenderer
		extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public HeadLayerFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);

	}

	private static int entityCounter = 0;

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
		
		PlayerSettings settings = (PlayerSettings) abstractClientPlayerEntity;
		// check for it being setup first to speedup the rendering
		if(settings.getHeadLayers() == null && !setupModel(abstractClientPlayerEntity, settings)) {
			return; // no head layer setup and wasn't able to setup
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider
				.getBuffer(RenderLayer.getEntityCutout((Identifier) abstractClientPlayerEntity.getSkinTexture()));
		int m = LivingEntityRenderer.getOverlay((LivingEntity) abstractClientPlayerEntity, (float) 0.0f);
		renderCustomHelmet(settings, abstractClientPlayerEntity, matrixStack, vertexConsumer, i, m);
	}

	private boolean setupModel(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerSettings settings) {
		
		if(!SkinUtil.hasCustomSkin(abstractClientPlayerEntity)) {
			return false; // default skin
		}
		NativeImage skin = SkinUtil.getSkinTexture(abstractClientPlayerEntity);
		settings.setupHeadLayers(SolidPixelWrapper.wrapBoxOptimized(skin, this.getContextModel(), 8, 8, 8, 32, 0, false, 0));
		skin.untrack();
		return true;
	}

	public void renderCustomHelmet(PlayerSettings settings, AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay) {
		matrixStack.push();
		this.getContextModel().head.rotate(matrixStack);
		matrixStack.scale(1.18f, 1.18f, 1.18f);
		settings.getHeadLayers().customRender(matrixStack, vertices, light, overlay);
		matrixStack.pop();

	}

	public static boolean isEnabled(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		LayerMode mode = FirstPersonModelMod.config.layerMode;
		if (mode == LayerMode.VANILLA2D || !abstractClientPlayerEntity.isPartVisible(PlayerModelPart.HAT))
			return false;
		ClientPlayerEntity thePlayer = MinecraftClient.getInstance().player;
		if (thePlayer == abstractClientPlayerEntity) {
			entityCounter = 0;
			return true;
		}
		//if(entityCounter > FirstPersonModelMod.config.layerLimiter)return false;
		if (mode != LayerMode.ONLYSELF) {
			int distance = FirstPersonModelMod.config.optimizedLayerDistance
					* FirstPersonModelMod.config.optimizedLayerDistance;
			boolean ret = thePlayer.getPos().squaredDistanceTo(abstractClientPlayerEntity.getPos()) < distance;
			if(ret)entityCounter++;
			return ret;
		}
		return false;
	}

}
