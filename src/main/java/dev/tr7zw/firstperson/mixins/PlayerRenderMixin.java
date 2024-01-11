package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

/**
 * Hides body parts and layers where needed
 *
 */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRenderMixin
        extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    /**
     * Just needed because of the extends
     * 
     * @param ctx
     * @param model
     * @param shadowRadius
     */
    public PlayerRenderMixin(Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void setModelPoseRedirect(PlayerRenderer playerEntityRenderer,
            AbstractClientPlayer player, AbstractClientPlayer abstractClientPlayerEntity_1, float f,
            float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        setModelProperties(player);
        if (FirstPersonModelCore.isRenderingPlayer) {
            PlayerModel<AbstractClientPlayer> model = this.getModel();
            model.head.visible = false;
            model.hat.visible = false;
            // Moved to HumanoidModelMixin to be called during setupAnim
//			((ModelPartBase)(Object)playerEntityModel_1.head).setHidden();
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
                model.leftArm.visible = false;
                model.leftSleeve.visible = false;
                model.rightArm.visible = false;
                model.rightSleeve.visible = false;
            } else {

            }
            if (FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
                model.body.visible = false;
            }
        }
    }

    /**
     * Undo the forced head hiding flag set during HumanoidModelMixin
     */
    @Inject(method = "render", at = @At(value = "RETURN"))
    public void render(AbstractClientPlayer abstractClientPlayerEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        ((ModelPartBase) (Object) this.getModel().head).showAgain();
        if (FirstPersonModelCore.isRenderingPlayer) {
            FirstPersonModelCore.isRenderingPlayer = false;
        }
    }

    @Shadow
    abstract void setModelProperties(AbstractClientPlayer abstractClientPlayerEntity_1);

}
