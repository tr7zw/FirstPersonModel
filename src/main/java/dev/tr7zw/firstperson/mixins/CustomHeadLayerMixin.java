package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.access.*;
import dev.tr7zw.firstperson.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.world.entity.*;

//lower prio to run before other mods
@Mixin(value = CustomHeadLayer.class, priority = 100)
public class CustomHeadLayerMixin {

    //? if >= 1.21.9 {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    //? } else {
    /*@Inject(method = "render", at = @At("HEAD"), cancellable = true)
    *///? }
       //? if >= 1.21.9 {

    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i,
            LivingEntityRenderState livingEntityRenderState, float f, float g, CallbackInfo info) {
        //? } else >= 1.21.3 {
        /*public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            LivingEntityRenderState livingEntityRenderState, float f, float g, CallbackInfo info) {
        *///? } else {
        /*
            public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntity livingEntity,
            float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        *///? }
           //? if >= 1.21.9 {
        if (((LivingEntityRenderStateAccess) livingEntityRenderState).isCameraEntity()) {
            //? } else {
            /*if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            *///? }
            info.cancel();
        }
    }

}
