package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

// EntityRenderDispatcher
@Mixin(EntityRendererManager.class)
public abstract class EntityRendererMangerMixin {

	@Shadow
	public static void renderShadow(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, Entity entityIn,
			float weightIn, float partialTicks, IWorldReader worldIn, float sizeIn) {
		
	}

	@Redirect(method = "renderEntityStatic", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getRenderOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/vector/Vector3d;"))
	private Vector3d getPosRedirect(EntityRenderer<Entity> entityRenderer, Entity entity, float tickDelta,
			Entity entity_1, double x, double y, double z, float yaw, float tickDelta_1, MatrixStack matrices,
			IRenderTypeBuffer vertexConsumers, int light) {
		if (entity instanceof AbstractClientPlayerEntity) {
			FirstPersonModelCore.instance.getWrapper().updatePositionOffset((AbstractClientPlayerEntity) entity,
					entityRenderer.getRenderOffset(entity, tickDelta), matrices);
			return (Vector3d) FirstPersonModelCore.instance.getWrapper().getOffset();
		} else {
			return entityRenderer.getRenderOffset(entity, tickDelta);
		}
	}

	@Redirect(method = "renderEntityStatic", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererManager;renderShadow(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/IWorldReader;F)V"))
	private void renderRedirect(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, Entity entityIn, float weightIn,
			float partialTicks, IWorldReader worldIn, float sizeIn) {
		if (entityIn instanceof AbstractClientPlayerEntity) {
			Vector3d offset = (Vector3d) FirstPersonModelCore.instance.getWrapper().getOffset();
			matrixStackIn.translate(offset.x, offset.y, offset.z);
			renderShadow(matrixStackIn, bufferIn, entityIn, weightIn, partialTicks, worldIn, sizeIn);
			matrixStackIn.translate(-offset.x, -offset.y, -offset.z);
		}
	}

}
