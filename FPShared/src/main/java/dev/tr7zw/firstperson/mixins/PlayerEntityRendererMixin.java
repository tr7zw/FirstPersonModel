package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

/**
 * Adds the custom FeatureRenderers and scales the player size
 *
 */
@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin
		extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public PlayerEntityRendererMixin(Context ctx, PlayerModel<AbstractClientPlayer> model,
            float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    private final Minecraft mc = Minecraft.getInstance();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {

	}

	@Inject(method = "scale", at = @At("HEAD"), cancellable = true)
	protected void scale(AbstractClientPlayer abstractClientPlayerEntity, PoseStack matrixStack, float f,
			CallbackInfo info) {
		if (abstractClientPlayerEntity == mc.player && (mc.options.getCameraType() != CameraType.FIRST_PERSON
				|| FirstPersonModelCore.config.cosmetic.modifyCameraHeight)) {
			float scaled = 0.9375f * ((float) FirstPersonModelCore.config.cosmetic.playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		} else if (abstractClientPlayerEntity != null && abstractClientPlayerEntity != mc.player) {
			float scaled = 0.9375f * ((float) ((PlayerSettings) abstractClientPlayerEntity).getCosmeticSettings().playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		}
	}

}
