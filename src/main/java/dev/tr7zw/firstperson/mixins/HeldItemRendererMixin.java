package dev.tr7zw.firstperson.mixins;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Hides the normal first person hands
 *
 */
@Mixin(ItemInHandRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    private EntityRenderDispatcher entityRenderDispatcher;
    @Shadow private float mainHandHeight;
    @Shadow private float offHandHeight;
    @Shadow private ItemStack mainHandItem;
    @Shadow private ItemStack offHandItem;

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    public void renderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
            float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, CallbackInfo info) {

        if (!FirstPersonModelCore.instance.isEnabled()) {
            return;
        }
        if (!FirstPersonModelCore.instance.getLogicHandler().showVanillaHands() && !FirstPersonModelCore.instance.getLogicHandler().vanillaHandsItem()) {
            info.cancel();
            return;
        }
        if (FirstPersonModelCore.instance.getLogicHandler().vanillaHandsItem() &&
                (item.isEmpty() /*TODO VANILLA HANDS ITEM*/|| (FirstPersonModelCore.instance.getConfig().dynamicHands && pitch > 35))) { //TODO DYNAMIC HAND
            info.cancel();
            return;
        }
        if (!FirstPersonModelCore.instance.getConfig().doubleHands
                || player.getMainHandItem().getItem() == Items.FILLED_MAP
                // spotless:off
            //#if MC >= 11700
                || player.isScoping()) {
            //#else
        	//$$|| false) {
            //#endif
            //spotless:on
            return;
        }
        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        matrices.pushPose();
        if (item.isEmpty() && !bl && !player.isInvisible()) {
            renderPlayerArm(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
        }
        matrices.popPose();
    }

    @Shadow
    public abstract void renderPlayerArm(PoseStack matrices, MultiBufferSource vertexConsumers, int light,
            float equipProgress, float swingProgress, HumanoidArm arm);

    /*public boolean skip() {//TODO NO NEED?
        return !FirstPersonModelCore.instance.isEnabled()
                || FirstPersonModelCore.instance.getLogicHandler().showVanillaHands();
    }*/

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F", shift = At.Shift.BEFORE), method = "tick", cancellable = true)
    public void tick(CallbackInfo ci) {//TODO DYNAMIC HAND
        if (FirstPersonModelCore.instance.isEnabled() && FirstPersonModelCore.instance.getConfig().vanillaHandsItem
                && FirstPersonModelCore.instance.getConfig().dynamicHands) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            float f = localPlayer.getXRot();
            if(f > 15) {
                if (f < 30) {
                    this.mainHandHeight = 15/f;
                    this.offHandHeight = 15/f;
                } else {
                    this.mainHandHeight -= this.mainHandHeight > -0.1f ? 0.15f : 0;
                    this.offHandHeight -= this.offHandHeight > -0.1f ? 0.15f : 0;
                }
                ci.cancel();
                this.mainHandItem = localPlayer.getMainHandItem();
                this.offHandItem = localPlayer.getOffhandItem();
            }
    }

}
