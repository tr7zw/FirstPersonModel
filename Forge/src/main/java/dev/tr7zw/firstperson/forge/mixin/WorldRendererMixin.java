package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;

// Same name in fabric \o/
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	private Minecraft client = Minecraft.getInstance();
	
	@Inject(at = @At("HEAD"), method = "renderEntity")
	private void renderEntity(Entity entity_1, double double_1, double double_2, double double_3, float float_1,
			MatrixStack matrixStack_1, IRenderTypeBuffer vertexConsumerProvider_1, CallbackInfo info) {
		if (FirstPersonModelMod.hideNextHeadArmor)
			FirstPersonModelMod.hideNextHeadArmor = false;
		if (FirstPersonModelCore.instance.isFixActive(entity_1, matrixStack_1))
			FirstPersonModelMod.hideNextHeadArmor = true; // if I don't wear any armor head then another helmet will be
															// hidden without this

		if (client.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON)
			return;
		if (entity_1 instanceof AbstractClientPlayerEntity) {
			if (!((PlayerEntity) entity_1).isUser())
				return;
			FirstPersonModelMod.isRenderingPlayer = true;
		}
	}

	@Inject(method = "Lnet/minecraft/client/renderer/WorldRenderer;updateCameraAndRender(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;isThirdPerson()Z"))
	private void updateCameraAndRender(MatrixStack matrixStackIn, float partialTicks, long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn,
			CallbackInfo ci) {
		FirstPersonModelCore.instance.getWrapper().isThirdPersonTrigger(matrixStackIn);
	}
}
