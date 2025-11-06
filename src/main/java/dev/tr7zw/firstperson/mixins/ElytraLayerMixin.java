package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.access.*;
import dev.tr7zw.firstperson.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.player.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
//? if >= 1.21.3 {

@Mixin(WingsLayer.class)
//? } else {

// @Mixin(ElytraLayer.class)
//? }
public class ElytraLayerMixin<T extends LivingEntity> {

    //? if >= 1.21.9 {

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i,
            HumanoidRenderState humanoidRenderState, float f, float g, CallbackInfo ci) {
        //? } else if >= 1.21.3 {
        /*@Inject(method = "render", at = @At("HEAD"), cancellable = true)
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            HumanoidRenderState humanoidRenderState, float f, float g, CallbackInfo ci) {
        *///? } else {

        //  @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        //  public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f,
        //          float g, float h, float j, float k, float l, CallbackInfo ci) {
        //? }
        //? if >= 1.21.9 {
        if (((LivingEntityRenderStateAccess) humanoidRenderState).isCameraEntity()
                && humanoidRenderState.isVisuallySwimming) {
            //? } else {
            /*if (FirstPersonModelCore.instance.isRenderingPlayer()
                && Minecraft.getInstance().getCameraEntity() instanceof AbstractClientPlayer player
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            
            *///? }

            ci.cancel();
        }
    }

}
