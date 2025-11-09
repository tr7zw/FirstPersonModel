package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.access.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
//? if >= 1.21.2
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.client.renderer.item.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

//? if < 1.21.4
/*import net.minecraft.world.item.*;*/

//? if >= 1.19.4 {

//? if < 1.21.4 {
/*
import net.minecraft.world.item.ItemDisplayContext;
*///? }
//? } else {
/*
 import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
*///? }

/**
 * Stops items in the hand from rendering while in first person.
 *
 */
@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {

    //? if >= 1.21.9 {
    @Inject(at = @At("HEAD"), method = "submitArmWithItem", cancellable = true)
    //? } else {
    /*@Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    *///? }
    //? if >= 1.21.9 {
    
    private void renderArmWithItem(ArmedEntityRenderState armedEntityRenderState,
            ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        //? } else if >= 1.21.4 {
    /*private void renderArmWithItem(ArmedEntityRenderState livingEntityRenderState,
            ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        *///? } else if >= 1.21.3 {

    // private void renderArmWithItem(LivingEntityRenderState livingEntityRenderState, BakedModel bakedModel,
    //        ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
    //        MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    //? } else if >= 1.19.4 {
/*
    private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack,
            ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        *///? } else {
        /*
           	private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType,
             			HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        *///? }
           //? if >= 1.21.9 {
           LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) armedEntityRenderState;
           if (access.hideLeftArm() && access.hideRightArm()
                   && !FirstPersonModelCore.instance.getLogicHandler().lookingDown(armedEntityRenderState)) {
               ci.cancel();
           }
           //? } else {
        /*if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(Minecraft.getInstance().player)
                    && !FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {
                ci.cancel();
            }
        }
        *///? }
    }

}
