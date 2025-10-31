package dev.tr7zw.firstperson.mixins;

import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.versionless.config.VanillaHands;
import dev.tr7zw.transition.mc.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
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
    @Shadow
    private float mainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private ItemStack mainHandItem;
    @Shadow
    private ItemStack offHandItem;

    @Shadow
    protected abstract void renderPlayerArm(PoseStack arg, SubmitNodeCollector arg2, int i, float g, float h,
            HumanoidArm arg3);

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    public void renderFirstPersonItem(AbstractClientPlayer player, float deltaTick, float pitch, InteractionHand hand,
            float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
            SubmitNodeCollector submitNodeCollector, int light, CallbackInfo info) {

        if (!FirstPersonModelCore.instance.isEnabled()) {
            return;
        }
        if (!FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
            info.cancel();
            return;
        }
        if (FirstPersonModelCore.instance.getConfig().vanillaHandsSkipSwimming
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            // while actively swimming, dont show the offhand item
            info.cancel();
            return;
        }
        // filter out vanilla hands with no item
        if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled() && pitch > 35) {
            // item held too low, hide
            info.cancel();
            return;
        }
        // double hands
        if (FirstPersonModelCore.instance.getConfig().vanillaHandsMode != VanillaHands.ALL_DOUBLE
                || player.getMainHandItem().getItem() == Items.FILLED_MAP
                //? if >= 1.17.0 {

                || player.isScoping()) {
            //? } else {

            // || false) {
            //? }
            return;
        }
        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        matrices.pushPose();
        if (item.isEmpty() && !bl && !player.isInvisible()) {
            renderPlayerArm(matrices, submitNodeCollector, light, equipProgress, swingProgress, arm);
        }
        matrices.popPose();
    }

    /*
     * public boolean skip() {//TODO NO NEED? return
     * !FirstPersonModelCore.instance.isEnabled() ||
     * FirstPersonModelCore.instance.getLogicHandler().showVanillaHands(); }
     */

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F", shift = At.Shift.BEFORE), method = "tick", cancellable = true)
    public void tick(CallbackInfo ci) {// TODO DYNAMIC HAND
        if (FirstPersonModelCore.instance.isEnabled()
                && FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()
                && FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            float f = EntityUtil.getXRot(localPlayer);
            if (f > 15) {
                if (f < 30) {
                    this.mainHandHeight = 15 / f;
                    this.offHandHeight = 15 / f;
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

}
