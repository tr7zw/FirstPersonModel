package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;

//? if >= 1.21.5 {

import net.minecraft.client.renderer.entity.state.HitboxRenderState;
//? }
//? if >= 1.21.3 {

import net.minecraft.client.renderer.entity.state.EntityRenderState;
//? } else {

// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.At.Shift;
// import org.spongepowered.asm.mixin.injection.Redirect;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import com.mojang.blaze3d.vertex.VertexConsumer;
// import net.minecraft.client.renderer.MultiBufferSource;
// import net.minecraft.util.Mth;
// import net.minecraft.world.entity.Entity;
// import net.minecraft.world.level.LevelReader;
// import net.minecraft.world.phys.Vec3;
//? }

/**
 * Move the first person shadow to be at the correct location
 *
 */
@Mixin(EntityRenderDispatcher.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RenderDispatcherMixin {

    /*private static Minecraft fpmMcInstance = Minecraft.getInstance();

//? if >= 1.21.3 {

    private static double tmpX;
    private static double tmpZ;

    @Inject(method = "renderShadow", at = @At("HEAD"))
    private static void renderShadow(PoseStack poseStack, MultiBufferSource multiBufferSource,
            EntityRenderState entityRenderState, float f,
//? if < 1.21.5 {

            // float g, 
//? }
            LevelReader levelReader, float h, CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            poseStack.pushPose();
            poseStack.translate(FirstPersonModelCore.instance.getLogicHandler().getOffset());
            tmpX = entityRenderState.x;
            tmpZ = entityRenderState.z;
            entityRenderState.x += FirstPersonModelCore.instance.getLogicHandler().getOffset().x;
            entityRenderState.z += FirstPersonModelCore.instance.getLogicHandler().getOffset().z;
        }
    }

    @Inject(method = "renderShadow", at = @At("RETURN"))
    private static void renderShadowEnd(PoseStack poseStack, MultiBufferSource multiBufferSource,
            EntityRenderState entityRenderState, float f,
//? if < 1.21.5 {

            // float g, 
//? }
            LevelReader levelReader, float h, CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            entityRenderState.x = tmpX;
            entityRenderState.z = tmpZ;
            poseStack.popPose();
        }
    }

    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
    private static void renderHitbox(PoseStack poseStack, VertexConsumer buffer,
//? if >= 1.21.5 {

            HitboxRenderState hitboxRenderState,
//? } else {

            // Entity entity, float red, float green, float blue, float alpha, 
//? }
            CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
            ci.cancel();
        }
    }

//? } else {

    //   @Redirect(method = "renderShadow", at = @At(value = "invoke", target = "Lnet/minecraft/util/Mth;lerp(DDD)D", ordinal = 0))
    //  private static double shadowOffsetX(double delta, double old, double cur, PoseStack poseStack,
    //          MultiBufferSource multiBufferSource, Entity entity, float f, float g, LevelReader levelReader, float h) {
    //      if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
    //          return Mth.lerp(delta, old, cur) + FirstPersonModelCore.instance.getLogicHandler().getOffset().x;
    //      }
    //       return Mth.lerp(delta, old, cur);
    //   }
    //
    //  @Redirect(method = "renderShadow", at = @At(value = "invoke", target = "Lnet/minecraft/util/Mth;lerp(DDD)D", ordinal = 2))
    //  private static double shadowOffsetZ(double delta, double old, double cur, PoseStack poseStack,
    //          MultiBufferSource multiBufferSource, Entity entity, float f, float g, LevelReader levelReader, float h) {
    //      if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
    //         return Mth.lerp(delta, old, cur) + FirstPersonModelCore.instance.getLogicHandler().getOffset().z;
    //      }
    //      return Mth.lerp(delta, old, cur);
    //  }
    //
    //   @Inject(method = "renderShadow", at = @At(value = "invoke", target = "Lcom/mojang/blaze3d/vertex/PoseStack;last()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;", shift = Shift.BEFORE))
    //   private static void shadowMove(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity, float opacity,
    //           float tickDelta, LevelReader world, float radius, CallbackInfo ci) {
    //      if (!FirstPersonModelCore.instance.isRenderingPlayerPost()) {
    //          return;
    //       }
    //       Vec3 offset = FirstPersonModelCore.instance.getLogicHandler().getOffset();
    //    matrices.translate(offset.x, offset.y, offset.z);
    // }
    //
    // @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
// //? if < 1.17.0 {

    // //  private void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f, CallbackInfo ci) {
// //? } else if < 1.21.0 {

    // // private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
    // //       CallbackInfo ci) {
// //? } else {

    // // private static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float f,
    // //       float g, float h, float i,
    // //               CallbackInfo ci) {
// //? }
    //    if (FirstPersonModelCore.instance.isRenderingPlayerPost()) {
    //       ci.cancel();
    //    }
    // }
//? }
*/

}
