package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import dev.tr7zw.firstperson.fabric.features.FabricFeature;
import dev.tr7zw.firstperson.fabric.features.boot.Boots1FeatureRenderer;
import dev.tr7zw.firstperson.fabric.features.hat.ItemHatFeatureRenderer;
import dev.tr7zw.firstperson.fabric.features.head.ItemHeadFeatureRenderer;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
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

	public PlayerEntityRendererMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model,
            float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    private final MinecraftClient mc = MinecraftClient.getInstance();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		for(AbstractCosmetic feature : FeatureProvider.getFeatures()) {
			this.addFeature(new FabricFeature(this, feature));
		}
		
		this.addFeature(new ItemHatFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(this));
		this.addFeature(new ItemHeadFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(this));
		this.addFeature(new Boots1FeatureRenderer(this));
	}

	@Inject(method = "scale", at = @At("HEAD"), cancellable = true)
	protected void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f,
			CallbackInfo info) {
		if (abstractClientPlayerEntity == mc.player && (mc.options.getPerspective() != Perspective.FIRST_PERSON
				|| FirstPersonModelMod.config.cosmetic.modifyCameraHeight)) {
			float scaled = 0.9375f * ((float) FirstPersonModelMod.config.cosmetic.playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		} else if (abstractClientPlayerEntity != null && abstractClientPlayerEntity != mc.player) {
			float scaled = 0.9375f * ((float) ((PlayerSettings) abstractClientPlayerEntity).getCosmeticSettings().playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		}
	}

}
