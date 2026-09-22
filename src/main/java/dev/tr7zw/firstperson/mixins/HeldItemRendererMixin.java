package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.firstperson.*;
import dev.tr7zw.firstperson.versionless.config.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.transition.mc.entitywrapper.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

/**
 * Hides the normal first person hands
 *
 */
//? if >= 26.3 {

@Mixin(FirstPersonHandsAndItemsRenderer.class)
//? } else {
/*
@Mixin(ItemInHandRenderer.class)
 */
//? }
public abstract class HeldItemRendererMixin {

    //? if < 26.3 {
    /*
    @Shadow
    private float mainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private ItemStack mainHandItem;
    @Shadow
    private ItemStack offHandItem;
     */
    //? }

    @Shadow
    //? if >= 26.3 {

    protected abstract void renderPlayerArm(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector,
            final int lightCoords, final float inverseArmHeight, final float attackValue, final HumanoidArm arm,
            final net.minecraft.client.renderer.state.level.PlayerRenderState playerState);
    //? } else if >= 1.21.9 {
    /*
    protected abstract void renderPlayerArm(PoseStack arg, SubmitNodeCollector arg2, int i, float g, float h,
            HumanoidArm arg3);
     */
    //? } else {
    /*public abstract void renderPlayerArm(PoseStack matrices, MultiBufferSource vertexConsumers, int light,
            float equipProgress, float swingProgress, HumanoidArm arm);
    *///? }

    //? if >= 26.3 {
    @Inject(at = @At("HEAD"), method = "submitArmWithItem", cancellable = true)
    public void renderFirstPersonItem(final net.minecraft.client.renderer.state.level.PlayerRenderState playerState,
            final net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState state,
            final float partialTicks, final float pitch, final InteractionHand hand, final float attack,
            final ItemStack item, final float inverseArmHeight, final PoseStack matrices,
            final SubmitNodeCollector submitNodeCollector, final int lightCoords, CallbackInfo info) {

        if (!FirstPersonModelCore.instance.isEnabled()) {
            return;
        }
        if (!FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
            info.cancel();
            return;
        }
        AbstractClientPlayer player = null;
        if (playerState.hasPlayer && ((EntityRenderStateExtender) playerState.avatarRenderState)
                .getTransitionEntity() instanceof AbstractClientPlayer p) {
            player = p;
        }
        if (player == null) {
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
                || player.getMainHandItem().getItem() == Items.FILLED_MAP || player.isScoping()) {
            return;
        }
        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        matrices.pushPose();
        if (item.isEmpty() && !bl && !player.isInvisible()) {
            renderPlayerArm(matrices, submitNodeCollector, lightCoords, inverseArmHeight, attack, arm, playerState);
        }
        matrices.popPose();
    }
    //? }

    //? if < 26.3 {
    /*
    //? if >= 26.2 {
    @Inject(at = @At("HEAD"), method = "submitArmWithItem", cancellable = true)
    //? } else {
    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
     //? }
        //? if >= 1.21.9 {
    
    public void renderFirstPersonItem(AbstractClientPlayer player, float deltaTick, float pitch, InteractionHand hand,
            float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
            SubmitNodeCollector vertexConsumers, int light, CallbackInfo info) {
    
        //? } else {
        public void renderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
            float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        //? }
    
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
    
             || false) {
            //? }
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
    
    @Inject(at = @At(value = "INVOKE", target =
            //? if >=1.21.11 {
            "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F"
            //? } else {
            "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"
            //? }
    , shift = At.Shift.BEFORE), method = "tick", cancellable = true)
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
     */
    //? }

}
