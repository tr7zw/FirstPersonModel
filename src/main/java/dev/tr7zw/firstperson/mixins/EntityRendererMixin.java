package dev.tr7zw.firstperson.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @WrapOperation(method = "finalizeRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;extractShadow(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/level/Level;)V"))
    private void offsetRendering(EntityRenderer<?, ?> instance, EntityRenderState entityRenderState, Minecraft mc, Level level, Operation<Void> original) {
        if (entityRenderState instanceof LivingEntityRenderStateAccess access && access.isCameraEntity()) {
            Vec3 vec3 = access.getRenderOffset();
            entityRenderState.x -= vec3.x;
            entityRenderState.z -= vec3.z;
            original.call(instance, entityRenderState, mc, level);
            entityRenderState.x += vec3.x;
            entityRenderState.z += vec3.z;
        } else {
            original.call(instance, entityRenderState, mc, level);
        }
    }
}
