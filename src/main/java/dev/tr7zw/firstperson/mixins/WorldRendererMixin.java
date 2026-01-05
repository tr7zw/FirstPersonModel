package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.*;
//? if >= 1.21.2 {
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.renderer.state.*;
//? }
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;
//? if >= 1.21.9 {
//? } else if >= 1.19.3 {
/*import org.joml.*;
*///? } else {
/*import com.mojang.math.*;
*///? }

/**
 * Detects when the player is rendered and triggers the correct changes
 *
 */
@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {

    //? if >= 1.21.9 {
    @Shadow
    protected abstract EntityRenderState extractEntity(Entity arg, float f);
    //? } else {
    /*@Shadow
    protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
            PoseStack matrices, MultiBufferSource vertexConsumers);
    *///? }

    @Shadow
    private RenderBuffers renderBuffers;

    //? if <= 1.20.4 {
    /*
        @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
        public void render(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
                GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
    *///? } else if < 1.21.0 {
    /*
    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    public void render(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
            GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2,
            CallbackInfo info) {
        PoseStack matrices = new PoseStack();
        *///? } else if < 1.21.3 {

    // @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0))
    // public void render(DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer,
    //    LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo info) {
    //    PoseStack matrices = new PoseStack();
    //? } else if < 1.21.9 {
    /*@Inject(method = "renderEntities", at = @At(value = "HEAD"))
    private void renderEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Camera camera,
        DeltaTracker deltaTracker, List<Entity> list, CallbackInfo ci) {
    PoseStack matrices = new PoseStack();
    *///? } else {

    @Inject(method = "extractVisibleEntities", at = @At(value = "HEAD"))
    private void renderEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker,
            LevelRenderState levelRenderState, CallbackInfo ci) {
        PoseStack matrices = new PoseStack();
        //? }
        if (camera.isDetached() || !FirstPersonModelCore.instance.getLogicHandler().shouldApplyThirdPerson(false)) {
            return;
        }
        Vec3 vec3d = /*? if >=1.21.11 {*/ camera.position() /*?} else {*//*camera.getPosition()*//*?}*/;
        MultiBufferSource.BufferSource immediate = renderBuffers.bufferSource();
        FirstPersonModelCore.instance.setRenderingPlayer(true);
        FirstPersonModelCore.instance.setRenderingPlayerPost(true);
        // Store position and apply offset
        var ent = /*? if >=1.21.11 {*/ camera.entity() /*?} else {*//*camera.getEntity()*//*?}*/;
        var pos = ((EntityAccessor) ent).entityCulling$getRawPosition();
        var xO = ent.xo;
        var yO = ent.yo;
        var zO = ent.zo;
        var xOld = ent.xOld;
        var yOld = ent.yOld;
        var zOld = ent.zOld;
        //? if >= 1.21.0{

        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(true);
        //? }
        FirstPersonModelCore.instance.getLogicHandler().updatePositionOffset(ent, tickDelta);
        var offset = FirstPersonModelCore.instance.getLogicHandler().getOffset();
        ((EntityAccessor) ent).entityCulling$setRawPosition(pos.add(offset));
        ent.xo += offset.x;
        ent.yo += offset.y;
        ent.zo += offset.z;
        ent.xOld += offset.x;
        ent.yOld += offset.y;
        ent.zOld += offset.z;
        // Trigger render
        //? if < 1.21.0 {
        /*
        renderEntity(camera.getEntity(), vec3d.x(), vec3d.y(), vec3d.z(), tickDelta, matrices, immediate);
        *///? } else if < 1.21.9 {
        /*
        FirstPersonModelCore.instance.getLogicHandler().updatePositionOffset(ent,
                deltaTracker.getGameTimeDeltaPartialTick(true));
        renderEntity(camera.getEntity(), vec3d.x(), vec3d.y(), vec3d.z(),
                deltaTracker.getGameTimeDeltaPartialTick(false), matrices, immediate);
        *///? } else {

        levelRenderState.entityRenderStates.add(extractEntity(ent, deltaTracker.getGameTimeDeltaPartialTick(true)));
        //? }
        // Restore position
        ((EntityAccessor) ent).entityCulling$setRawPosition(pos);
        ent.refreshDimensions(); // Fix corrupted state caused during rendering by other mods (NEA apparently)
        ent.xo = xO;
        ent.yo = yO;
        ent.zo = zO;
        ent.xOld = xOld;
        ent.yOld = yOld;
        ent.zOld = zOld;
        FirstPersonModelCore.instance.setRenderingPlayer(false);
        FirstPersonModelCore.instance.setRenderingPlayerPost(false);

    }

}
