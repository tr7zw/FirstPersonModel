package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.LivingEntity;

//lower prio to run before other mods
@Mixin(value = CustomHeadLayer.class, priority = 100)
public class CustomHeadLayerMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity,
            float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            info.cancel();
        }
    }

}
