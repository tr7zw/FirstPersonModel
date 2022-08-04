package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

/**
 * Move the first person shadow to be at the correct location
 *
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class RenderDispatcherMixin {

    private static Minecraft fpm_mc = Minecraft.getInstance();
    
    @Redirect(method = "renderShadow", at = @At(value = "invoke", target = "Lnet/minecraft/util/Mth;lerp(DDD)D", ordinal = 0))
    private static double shadowOffsetX(double delta, double old, double cur, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, float f,
            float g, LevelReader levelReader, float h) {
        if(entity == fpm_mc.cameraEntity) {
            return Mth.lerp(delta, old, cur) + FirstPersonModelCore.getWrapper().getOffset().x;
        }
        return Mth.lerp(delta, old, cur);
    }
    
    @Redirect(method = "renderShadow", at = @At(value = "invoke", target = "Lnet/minecraft/util/Mth;lerp(DDD)D", ordinal = 2))
    private static double shadowOffsetZ(double delta, double old, double cur, PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, float f,
            float g, LevelReader levelReader, float h) {
        if(entity == fpm_mc.cameraEntity) {
            return Mth.lerp(delta, old, cur) + FirstPersonModelCore.getWrapper().getOffset().z;
        }
        return Mth.lerp(delta, old, cur);
    }
    
    @Inject(method = "renderShadow", at = @At(value = "invoke", target = "Lcom/mojang/blaze3d/vertex/PoseStack;last()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;", shift = Shift.BEFORE))
    private static void shadowMove(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity, float opacity, float tickDelta, LevelReader world, float radius, CallbackInfo ci) {
        if(entity != fpm_mc.cameraEntity) {
            return;
        }
        Vec3 offset = FirstPersonModelCore.getWrapper().getOffset();
        matrices.translate(offset.x, offset.y, offset.z);
    }

}
