package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

// Same name in fabric \o/
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	
	@Shadow
	private RenderTypeBuffers renderTypeTextures;

	
	@Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;checkMatrixStack(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", ordinal = 0))
	public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, ActiveRenderInfo camera,
			GameRenderer gameRenderer, LightTexture lightmapIn, Matrix4f matrix4f, CallbackInfo info) {
		if(camera.isThirdPerson() || !FirstPersonModelCore.getWrapper().applyThirdPerson(false))return;
		Vector3d vec3d = camera.getProjectedView();
		double d = vec3d.getX();
		double e = vec3d.getY();
		double f = vec3d.getZ();
		Impl immediate = this.renderTypeTextures.getBufferSource();
		FirstPersonModelMod.isRenderingPlayer = true;
		this.renderEntity(camera.getRenderViewEntity(), d, e, f, tickDelta, matrices, immediate);
		FirstPersonModelMod.isRenderingPlayer = false;
	}

	@Shadow
	   private void renderEntity(Entity entityIn, double camX, double camY, double camZ, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
		
	}
	
	
}
