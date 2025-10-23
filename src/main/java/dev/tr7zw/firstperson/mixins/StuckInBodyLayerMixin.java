package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
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
//import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin<T extends LivingEntity> {

    //#if MC >= 12103
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void disableStuckFeatureLayer(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, AvatarRenderState avatarRenderState, float f, float g, CallbackInfo ci) {
        //#else
        //$$@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        //$$public void disableStuckFeatureLayer(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
        //$$ T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        //#endif   
        if (((LivingEntityRenderStateAccess) avatarRenderState).isCameraEntity()
                && !FirstPersonModelCore.instance.getConfig().renderStuckFeatures) {
            ci.cancel();
        }
    }

}
