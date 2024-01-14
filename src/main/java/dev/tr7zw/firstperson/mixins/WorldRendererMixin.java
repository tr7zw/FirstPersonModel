package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
// spotless:off
//#if MC >= 11903
import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif
//spotless:on

/**
 * Detects when the player is rendered and triggers the correct changes
 *
 */
@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private RenderBuffers renderBuffers;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    public void render(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
            GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
        if (camera.isDetached() || !FirstPersonModelCore.instance.getLogicHandler().shouldApplyThirdPerson(false))
            return;
        Vec3 vec3d = camera.getPosition();
        MultiBufferSource.BufferSource immediate = this.renderBuffers.bufferSource();
        FirstPersonModelCore.isRenderingPlayer = true;
        this.renderEntity(camera.getEntity(), vec3d.x(), vec3d.y(), vec3d.z(), tickDelta, matrices,
                (MultiBufferSource) immediate);
        FirstPersonModelCore.isRenderingPlayer = false;
    }

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
            PoseStack matrices, MultiBufferSource vertexConsumers) {

    }

}
