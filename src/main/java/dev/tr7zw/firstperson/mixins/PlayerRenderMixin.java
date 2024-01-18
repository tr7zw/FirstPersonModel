package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerModelAccess;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
//spotless:off
//#if MC >= 11700
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
//#else
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//#endif
//spotless:on
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

/**
 * Hides body parts and layers where needed
 *
 */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRenderMixin
        extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    // spotless:off
    //#if MC >= 11700
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
    //#else
  //$$ public PlayerRenderMixin(EntityRenderDispatcher entityRenderDispatcher) {
  //$$	super(null, null, 0);
  //$$ }
	//#endif
	//spotless:on

    /**
     * Undo the forced head hiding flag set during HumanoidModelMixin
     */
    @Inject(method = "render", at = @At(value = "RETURN"))
    public void render(AbstractClientPlayer player, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        ((ModelPartBase) (Object) this.getModel().head).showAgain();
        ((ModelPartBase) (Object) this.getModel().hat).showAgain();
        ((ModelPartBase) (Object) this.getModel().leftArm).showAgain();
        ((ModelPartBase) (Object) this.getModel().rightArm).showAgain();
        ((ModelPartBase) (Object) this.getModel().leftSleeve).showAgain();
        ((ModelPartBase) (Object) this.getModel().rightSleeve).showAgain();
        ((ModelPartBase) (Object) this.getModel().body).showAgain();
        ((ModelPartBase) (Object) ((PlayerModelAccess) model).getCloak()).showAgain();
        FirstPersonModelCore.instance.setRenderingPlayer(false);
    }

    @Shadow
    abstract void setModelProperties(AbstractClientPlayer abstractClientPlayerEntity_1);

}
