package dev.tr7zw.firstperson.mixins;

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
import net.minecraft.world.item.ItemStack;
//#if MC >= 12103
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//#else
//$$import net.minecraft.client.player.LocalPlayer;
//$$import net.minecraft.world.entity.LivingEntity;
//#endif
//#if MC >= 11904
import net.minecraft.world.item.ItemDisplayContext;
//#else
//$$ import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//#endif

/**
 * Stops items in the hand from rendering while in first person.
 *
 */
@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    //#if MC >= 12103
    private void renderArmWithItem(LivingEntityRenderState livingEntityRenderState, BakedModel bakedModel,
            ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //#elseif MC >= 11904
        //$$private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext,
        //$$        HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //#else
        //$$   	private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType,
        //$$     			HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        //#endif
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(Minecraft.getInstance().player)
                    && !FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {
                ci.cancel();
            }
        }
    }

}
