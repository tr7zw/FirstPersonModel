package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

/**
 * Move the first person shadow to be at the correct location
 *
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class RenderDispatcherMixin {

    @Shadow
    @Final
    private static RenderType SHADOW_RENDER_TYPE;

    @Shadow
    private static void renderShadow(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity,
            float opacity, float tickDelta, LevelReader world, float radius) {
    }

    @Shadow
    private static void renderBlockShadow(PoseStack.Pose entry, VertexConsumer vertices, LevelReader world,
            BlockPos pos, double x, double y, double z, float radius, float opacity) {
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getRenderOffset(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 getPosRedirect(EntityRenderer<Entity> entityRenderer, Entity entity, float tickDelta, Entity entity_1,
            double x, double y, double z, float yaw, float tickDelta_1, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light) {
        if (entity instanceof AbstractClientPlayer) {
            FirstPersonModelCore.getWrapper().updatePositionOffset(entity,
                    entityRenderer.getRenderOffset(entity, tickDelta), matrices);
            return (Vec3) FirstPersonModelCore.getWrapper().getOffset();
        } else {
            return entityRenderer.getRenderOffset(entity, tickDelta);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;FFLnet/minecraft/world/level/LevelReader;F)V"))
    private void renderRedirect(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity,
            float opacity, float tickDelta, LevelReader world, float radius) {
        if (entity instanceof AbstractClientPlayer) {
            Vec3 offset = (Vec3) FirstPersonModelCore.getWrapper().getOffset();
            renderOffsetShadow(matrices, vertexConsumers, entity, opacity, tickDelta, world, radius, offset);
        } else {
            renderShadow(matrices, vertexConsumers, entity, opacity, tickDelta, world, radius);
        }
    }

    private static void renderOffsetShadow(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity,
            float opacity, float tickDelta, LevelReader world, float radius, Vec3 offset) {
        float f = radius;
        if (entity instanceof Mob mobEntity) {
            if (mobEntity.isBaby())
                f *= 0.5F;
        }
        double d = Mth.lerp(tickDelta, entity.xOld, entity.getX()) + offset.x;
        double e = Mth.lerp(tickDelta, entity.yOld, entity.getY()) + offset.y;
        double g = Mth.lerp(tickDelta, entity.zOld, entity.getZ()) + offset.z;
        int i = Mth.floor(d - f);
        int j = Mth.floor(d + f);
        int k = Mth.floor(e - f);
        int l = Mth.floor(e);
        int m = Mth.floor(g - f);
        int n = Mth.floor(g + f);
        matrices.translate(offset.x, offset.y, offset.z);
        matrices.pushPose();
        PoseStack.Pose entry = matrices.last();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(SHADOW_RENDER_TYPE);
        for (BlockPos blockPos : BlockPos.betweenClosed(new BlockPos(i, k, m), new BlockPos(j, l, n)))
            renderBlockShadow(entry, vertexConsumer, world, blockPos, d, e, g, f, opacity);
        matrices.popPose();
    }

}
