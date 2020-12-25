package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

/**
 * Move the first person shadow to be at the correct location
 *
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class RenderDispatcherMixin {
	@Shadow
	protected static void renderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity,
			float opacity, float tickDelta, WorldView world, float radius) {
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;getPositionOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d getPosRedirect(EntityRenderer<Entity> entityRenderer, Entity entity, float tickDelta, Entity entity_1,
			double x, double y, double z, float yaw, float tickDelta_1, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light) {
		if (entity instanceof AbstractClientPlayerEntity) {
			FirstPersonModelCore.instance.getWrapper().updatePositionOffset((AbstractClientPlayerEntity) entity,
					entityRenderer.getPositionOffset(entity, tickDelta), matrices);
			return (Vec3d) FirstPersonModelCore.instance.getWrapper().getOffset();
		} else {
			return entityRenderer.getPositionOffset(entity, tickDelta);
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V"))
	private void renderRedirect(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity,
			float opacity, float tickDelta, WorldView world, float radius) {
		if (entity instanceof AbstractClientPlayerEntity) {
			Vec3d offset = (Vec3d) FirstPersonModelCore.instance.getWrapper().getOffset();
			matrices.translate(offset.x, offset.y, offset.z);
			renderShadow(matrices, vertexConsumers, entity, opacity, tickDelta, world, radius);
			matrices.translate(-offset.x, -offset.y,
					-offset.z);
		}
	}
}
