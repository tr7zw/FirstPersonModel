package de.tr7zw.firstperson.mixins;

import de.tr7zw.firstperson.StaticMixinMethods;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public class RenderDispatcherMixin {
    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderer;getPositionOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;"
    ))
    private Vec3d getPosRedirect(EntityRenderer entityRenderer, Entity entity, float tickDelta, Entity entity_1, double x, double y, double z, float yaw, float tickDelta_1, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        if(entity instanceof AbstractClientPlayerEntity) {
            return StaticMixinMethods.getPositionOffset((AbstractClientPlayerEntity) entity, entityRenderer.getPositionOffset(entity, tickDelta), matrices);
        }
        else {
            return entityRenderer.getPositionOffset(entity, tickDelta);
        }
    }
}
