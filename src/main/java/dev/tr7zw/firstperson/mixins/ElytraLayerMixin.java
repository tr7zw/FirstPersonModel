package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
//? if >= 1.21.3 {

import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//? } else {

// import net.minecraft.client.renderer.entity.layers.ElytraLayer;
//? }
import net.minecraft.world.entity.LivingEntity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
//? if >= 1.21.3 {

@Mixin(WingsLayer.class)
//? } else {

// @Mixin(ElytraLayer.class)
//? }
public class ElytraLayerMixin<T extends LivingEntity> {

    //? if >= 1.21.3 {

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i,
            HumanoidRenderState humanoidRenderState, float f, float g, CallbackInfo ci) {
        //? } else {

        //  @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        //  public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f,
        //          float g, float h, float j, float k, float l, CallbackInfo ci) {
        //? }
        if (((LivingEntityRenderStateAccess) humanoidRenderState).isCameraEntity()
                && humanoidRenderState.isVisuallySwimming) {
            ci.cancel();
        }
    }

}
