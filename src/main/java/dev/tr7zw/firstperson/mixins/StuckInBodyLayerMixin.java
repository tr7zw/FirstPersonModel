package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.world.entity.LivingEntity;
//#if MC >= 12103
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin<T extends LivingEntity> {

    //#if MC >= 12103
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void disableStuckFeatureLayer(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            PlayerRenderState playerRenderState, float f, float g, CallbackInfo ci) {
        //#else
        //$$@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        //$$public void disableStuckFeatureLayer(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
        //$$ T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        //#endif   
        if (FirstPersonModelCore.instance.isRenderingPlayer()
                && !FirstPersonModelCore.instance.getConfig().renderStuckFeatures) {
            ci.cancel();
        }
    }

}
