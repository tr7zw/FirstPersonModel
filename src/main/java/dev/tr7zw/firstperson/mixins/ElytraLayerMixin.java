package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
//#if MC >= 12103
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//#else
//$$import net.minecraft.client.renderer.entity.layers.ElytraLayer;
//#endif
import net.minecraft.world.entity.LivingEntity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 */
//#if MC >= 12103
@Mixin(WingsLayer.class)
//#else
//$$@Mixin(ElytraLayer.class)
//#endif
public class ElytraLayerMixin<T extends LivingEntity> {

    //#if MC >= 12103
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i,
            HumanoidRenderState humanoidRenderState, float f, float g, CallbackInfo ci) {
        //#else
        //$$  @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        //$$  public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f,
        //$$          float g, float h, float j, float k, float l, CallbackInfo ci) {
        //#endif
        if (FirstPersonModelCore.instance.isRenderingPlayer()
                && Minecraft.getInstance().getCameraEntity() instanceof AbstractClientPlayer player
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            ci.cancel();
        }
    }

}
