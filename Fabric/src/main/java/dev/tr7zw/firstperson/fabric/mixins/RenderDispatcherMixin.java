package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

/**
 * Move the first person shadow to be at the correct location
 *
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class RenderDispatcherMixin {

    @Shadow
    private static RenderLayer SHADOW_LAYER;

    @Shadow
    protected static void renderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity,
            float opacity, float tickDelta, WorldView world, float radius) {
    }

    @Shadow
    protected static void renderShadowPart(MatrixStack.Entry entry, VertexConsumer vertices, WorldView world,
            BlockPos pos, double x, double y, double z, float radius, float opacity) {
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;getPositionOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d getPosRedirect(EntityRenderer<Entity> entityRenderer, Entity entity, float tickDelta, Entity entity_1,
            double x, double y, double z, float yaw, float tickDelta_1, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        if (entity instanceof AbstractClientPlayerEntity) {
            FirstPersonModelCore.getWrapper().updatePositionOffset((AbstractClientPlayerEntity) entity,
                    entityRenderer.getPositionOffset(entity, tickDelta), matrices);
            return (Vec3d) FirstPersonModelCore.getWrapper().getOffset();
        } else {
            return entityRenderer.getPositionOffset(entity, tickDelta);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V"))
    private void renderRedirect(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity,
            float opacity, float tickDelta, WorldView world, float radius) {
        if (entity instanceof AbstractClientPlayerEntity) {
            Vec3d offset = (Vec3d) FirstPersonModelCore.getWrapper().getOffset();
            renderOffsetShadow(matrices, vertexConsumers, entity, opacity, tickDelta, world, radius, offset);
        } else {
            renderShadow(matrices, vertexConsumers, entity, opacity, tickDelta, world, radius);
        }
    }

    private static void renderOffsetShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity,
            float opacity, float tickDelta, WorldView world, float radius, Vec3d offset) {
        float f = radius;
        if (entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            if (mobEntity.isBaby())
                f *= 0.5F;
        }
        double d = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX()) + offset.x;
        double e = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY()) + offset.y;
        double g = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ()) + offset.z;
        int i = MathHelper.floor(d - f);
        int j = MathHelper.floor(d + f);
        int k = MathHelper.floor(e - f);
        int l = MathHelper.floor(e);
        int m = MathHelper.floor(g - f);
        int n = MathHelper.floor(g + f);
        matrices.translate(offset.x, offset.y, offset.z);
        matrices.push();
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(SHADOW_LAYER);
        for (BlockPos blockPos : BlockPos.iterate(new BlockPos(i, k, m), new BlockPos(j, l, n)))
            renderShadowPart(entry, vertexConsumer, world, blockPos, d, e, g, f, opacity);
        matrices.pop();
    }

}
