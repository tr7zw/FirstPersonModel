package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
//spotless:off
//#if MC >= 11904
import net.minecraft.world.item.ItemDisplayContext;
//#else
//$$ import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//#endif
//spotless:on

/**
 * Stops items in the hand from rendering while in first person.
 *
 */
@Mixin(ItemInHandLayer.class)
public class HeldItemFeatureRendererMixin {

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    // spotless:off
    //#if MC >= 11904
      private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext,
              HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
      	//#else
    	//$$   	private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType,
    	//$$     			HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
      	//#endif
      	//spotless:on
        if (livingEntity instanceof LocalPlayer && FirstPersonModelCore.instance.isRenderingPlayer()
                && FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
            ci.cancel();
        }
    }

}
