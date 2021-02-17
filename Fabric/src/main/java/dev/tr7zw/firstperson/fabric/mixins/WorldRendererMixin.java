package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.fabric.FirstPersonModelMod;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

/**
 * Detects when the player is rendered and triggers the correct changes
 *
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	private BufferBuilderStorage bufferBuilders;
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
	public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
			GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
		if(camera.isThirdPerson() || !FirstPersonModelCore.getWrapper().applyThirdPerson(false))return;
		Vec3d vec3d = camera.getPos();
		double d = vec3d.getX();
		double e = vec3d.getY();
		double f = vec3d.getZ();
		VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
		FirstPersonModelMod.isRenderingPlayer = true;
		this.renderEntity(camera.getFocusedEntity(), d, e, f, tickDelta, matrices, (VertexConsumerProvider) immediate);
	}

	@Shadow
	private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		
	}
	
}
