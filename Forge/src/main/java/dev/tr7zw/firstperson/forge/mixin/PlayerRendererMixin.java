package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureProvider;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import dev.tr7zw.firstperson.forge.features.ForgeFeature;
import dev.tr7zw.firstperson.mixinbase.ModelPartBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.settings.PointOfView;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin
		extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public PlayerRendererMixin(EntityRendererManager rendererManager,
			PlayerModel<AbstractClientPlayerEntity> entityModelIn, float shadowSizeIn) {
		super(rendererManager, entityModelIn, shadowSizeIn);
	}

	private final Minecraft mc = Minecraft.getInstance();

	@Inject(method = "<init>*", at = @At("RETURN"))
	public void onCreate(CallbackInfo info) {
		for (AbstractCosmetic feature : FeatureProvider.getFeatures()) {
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
			float scaled = 0.9375f
					* ((float) ((PlayerSettings) abstractClientPlayerEntity).getCosmeticSettings().playerSize / 100f);
			matrixStack.scale(scaled, scaled, scaled);
			info.cancel();
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/PlayerRenderer;setModelVisibilities(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V"))
	private void setModelPoseRedirect(PlayerRenderer playerEntityRenderer,
			AbstractClientPlayerEntity abstractClientPlayerEntity,
			AbstractClientPlayerEntity abstractClientPlayerEntity_1, float f, float g, MatrixStack matrixStack,
			IRenderTypeBuffer vertexConsumerProvider, int i) {
		setModelVisibilities(abstractClientPlayerEntity);
		if (FirstPersonModelCore.isRenderingPlayer) {
			PlayerModel<AbstractClientPlayerEntity> playerEntityModel_1 = this.getEntityModel();
			playerEntityModel_1.bipedHead.showModel = false;
			playerEntityModel_1.bipedHeadwear.showModel = false;
			((ModelPartBase) playerEntityModel_1.bipedHead).setHidden();
			((ModelPartBase) playerEntityModel_1.bipedHeadwear).setHidden();
			if (FirstPersonModelMod.config.firstPerson.vanillaHands) {
				playerEntityModel_1.bipedLeftArm.showModel = false;
				playerEntityModel_1.bipedLeftArmwear.showModel = false;
				playerEntityModel_1.bipedRightArm.showModel = false;
				playerEntityModel_1.bipedRightArmwear.showModel = false;
			}
		}
	}

	@Inject(method = "render", at = @At(value = "RETURN"))
	public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack,
			IRenderTypeBuffer vertexConsumerProvider, int i, CallbackInfo info) {
		((ModelPartBase) this.getEntityModel().bipedHead).showAgain();
		((ModelPartBase) this.getEntityModel().bipedHeadwear).showAgain();
		FirstPersonModelMod.isRenderingPlayer = false;
	}

	@Shadow
	private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {

	}

}
