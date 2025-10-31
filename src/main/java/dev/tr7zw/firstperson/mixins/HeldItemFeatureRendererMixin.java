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

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
//? if < 1.21.4 {

// import net.minecraft.world.item.ItemStack;
//? }
//? if = 12103 {

// import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//? }
//? if >= 1.21.3 && < 1.21.4 {

// import net.minecraft.client.resources.model.BakedModel;
//? } else {

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
//? }
//? if >= 1.19.4 {

//? if < 1.21.4 {

// import net.minecraft.world.item.ItemDisplayContext;
//? }
//? } else {

// import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//? }
//? if >= 1.21.4 {

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
//? }

/**
 * Stops items in the hand from rendering while in first person.
 *
 */
@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {

    @Inject(at = @At("HEAD"), method = "submitArmWithItem", cancellable = true)
    //? if >= 1.21.4 {

    private void renderArmWithItem(ArmedEntityRenderState armedEntityRenderState,
            ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        //? } else if >= 1.21.3 {

        // private void renderArmWithItem(LivingEntityRenderState livingEntityRenderState, BakedModel bakedModel,
        //        ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
        //        MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //? } else if >= 1.19.4 {

        // private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext,
        //        HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //? } else {

        //   	private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType,
        //     			HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //? }
        LivingEntityRenderStateAccess access = (LivingEntityRenderStateAccess) armedEntityRenderState;
        if (access.hideLeftArm() && access.hideRightArm()
                && !FirstPersonModelCore.instance.getLogicHandler().lookingDown(armedEntityRenderState)) {
            ci.cancel();
        }
    }

}
