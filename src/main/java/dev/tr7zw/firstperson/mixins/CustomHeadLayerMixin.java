package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
//#if MC >= 12103
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//#else
//$$import net.minecraft.world.entity.LivingEntity;
//#endif

//lower prio to run before other mods
@Mixin(value = CustomHeadLayer.class, priority = 100)
public class CustomHeadLayerMixin {

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    //#if MC >= 12103
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, LivingEntityRenderState livingEntityRenderState, float f, float g, CallbackInfo info) {
        //#else
        //$$public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity,
        //$$        float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        //#endif
        if (((LivingEntityRenderStateAccess) livingEntityRenderState).isCameraEntity()) {
            info.cancel();
        }
    }

}
