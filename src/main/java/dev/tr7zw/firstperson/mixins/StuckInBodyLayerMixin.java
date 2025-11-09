package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin<T extends LivingEntity> {

    //? if >= 1.21.9 {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void disableStuckFeatureLayer(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i,
            AvatarRenderState avatarRenderState, float f, float g, CallbackInfo ci) {
        //? } else if >= 1.21.3 {
    /*
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void disableStuckFeatureLayer(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            PlayerRenderState playerRenderState, float f, float g, CallbackInfo ci) {
    
        *///? } else {
/*
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void disableStuckFeatureLayer(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        *///? }
        //? if >= 1.21.9 {
        if (((LivingEntityRenderStateAccess) avatarRenderState).isCameraEntity()
                //? } else {
        /*if (FirstPersonModelCore.instance.isRenderingPlayer()
                *///? }
                && !FirstPersonModelCore.instance.getConfig().renderStuckFeatures) {
            ci.cancel();
        }
    }

}
