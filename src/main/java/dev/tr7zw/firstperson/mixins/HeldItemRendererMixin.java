package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.player.AbstractClientPlayer;
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

    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    public void renderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand,
            float swingProgress, ItemStack item, float equipProgress, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        if (!skip()) {
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

    public boolean skip() {
        return !FirstPersonModelCore.instance.isEnabled()
                || FirstPersonModelCore.instance.getLogicHandler().showVanillaHands();
    }

}
