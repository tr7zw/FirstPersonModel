package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureProvider;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import dev.tr7zw.firstperson.forge.features.ForgeFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.settings.PointOfView;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public PlayerRendererMixin(EntityRendererManager rendererManager,
			PlayerModel<AbstractClientPlayerEntity> entityModelIn, float shadowSizeIn) {
		super(rendererManager, entityModelIn, shadowSizeIn);
	}

	private final Minecraft mc = Minecraft.getInstance();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		for(AbstractCosmetic feature : FeatureProvider.getFeatures()) {
			addLayer(new ForgeFeature(this, feature));
		}
	}

	@Inject(method = "preRenderCallback", at = @At("HEAD"), cancellable = true)
	protected void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f,
			CallbackInfo info) {
		if (abstractClientPlayerEntity == mc.player && (mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON
				|| FirstPersonModelMod.config.cosmetic.modifyCameraHeight)) {
			float scaled = 0.9375f * ((float) FirstPersonModelMod.config.cosmetic.playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		} else if (abstractClientPlayerEntity != mc.player) {
			float scaled = 0.9375f * ((float) ((PlayerSettings) abstractClientPlayerEntity).getCosmeticSettings().playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		}
	}
	
}
