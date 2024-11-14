package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
//#if MC >= 12103
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
//#endif
//#if MC >= 12100
import net.minecraft.client.DeltaTracker;

import java.util.List;

//#endif
//#if MC >= 11903
import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif

/**
 * Detects when the player is rendered and triggers the correct changes
 *
 */
@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private RenderBuffers renderBuffers;

    //#if MC <= 12004
    //$$    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    //$$    public void render(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
    //$$            GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
    //#elseif MC < 12100
    //$$ @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    //$$ public void render(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
    //$$        GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo info) {
    //$$ PoseStack matrices = new PoseStack();
    //#elseif MC < 12103
    //$$ @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    //$$ public void render(DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer,
    //$$    LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo info) {
    //$$    PoseStack matrices = new PoseStack();
    //#else
    @Inject(method = "renderEntities", at = @At(value = "HEAD"))
    private void renderEntities(PoseStack poseStack, BufferSource bufferSource, Camera camera,
            DeltaTracker deltaTracker, List<Entity> list, CallbackInfo ci) {
        PoseStack matrices = new PoseStack();
        //#endif
        if (camera.isDetached() || !FirstPersonModelCore.instance.getLogicHandler().shouldApplyThirdPerson(false)) {
            return;
        }
        Vec3 vec3d = camera.getPosition();
        MultiBufferSource.BufferSource immediate = renderBuffers.bufferSource();
        FirstPersonModelCore.instance.setRenderingPlayer(true);
        // spotless:off
        //#if MC < 12100
        //$$ renderEntity(camera.getEntity(), vec3d.x(), vec3d.y(), vec3d.z(), tickDelta, matrices, immediate);
        //#else
        renderEntity(camera.getEntity(), vec3d.x(), vec3d.y(), vec3d.z(), deltaTracker.getGameTimeDeltaPartialTick(false), matrices, immediate);
        //#endif
        //spotless:on
        FirstPersonModelCore.instance.setRenderingPlayer(false);
    }

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
            PoseStack matrices, MultiBufferSource vertexConsumers) {
        // shadow
    }

}
