package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.boot.Boots1FeatureRenderer;
import de.tr7zw.firstperson.features.chest.FemaleFeatureRenderer;
import de.tr7zw.firstperson.features.hat.Deadmau5EarsFeatureRenderer;
import de.tr7zw.firstperson.features.hat.ItemHatFeatureRenderer;
import de.tr7zw.firstperson.features.head.ItemHeadFeatureRenderer;
import de.tr7zw.firstperson.features.layers.BodyLayerFeatureRenderer;
import de.tr7zw.firstperson.features.layers.HeadLayerFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.options.Perspective;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Adds the custom FeatureRenderers and scales the player size
 *
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
		extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher,
			PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(dispatcher, model, shadowRadius);
	}

	private final MinecraftClient mc = MinecraftClient.getInstance();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		this.addFeature(new Deadmau5EarsFeatureRenderer(this));
		this.addFeature(new ItemHatFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(this));
		this.addFeature(new ItemHeadFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(this));
		this.addFeature(new Boots1FeatureRenderer(this));
		this.addFeature(new FemaleFeatureRenderer(this));

		this.addFeature(new HeadLayerFeatureRenderer(this));
		this.addFeature(new BodyLayerFeatureRenderer(this));
	}

	@Inject(method = "scale", at = @At("HEAD"), cancellable = true)
	protected void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f,
			CallbackInfo info) {
		if (abstractClientPlayerEntity == mc.player && (mc.options.getPerspective() != Perspective.FIRST_PERSON
				|| FirstPersonModelMod.config.modifyCameraHeight)) {
			float scaled = 0.9375f * ((float) FirstPersonModelMod.config.playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		} else if (abstractClientPlayerEntity != mc.player) {
			float scaled = 0.9375f * ((float) ((PlayerSettings) abstractClientPlayerEntity).getCustomHeight() / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		}
	}

}
