package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerModelAccess;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Updating the head hiding state during the animation setup, for better
 * compatebility with mods like Essential.
 * 
 * @author tr7zw
 *
 * @param <T>
 */
@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

    @Shadow
    ModelPart head;
    @Shadow
    ModelPart rightArm;
    @Shadow
    ModelPart leftArm;

    @Inject(method = "setupAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V"))
    public void hideHead(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            ((ModelPartBase) (Object) head).setHidden();
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
                ((ModelPartBase) (Object) leftArm).setHidden();
                ((ModelPartBase) (Object) rightArm).setHidden();
            }
        }
        if (FirstPersonModelCore.instance.isRenderingPlayer() && (Object) this instanceof PlayerModel<?> playerModel) {
            ((ModelPartBase) (Object) playerModel.hat).setHidden();
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
                ((ModelPartBase) (Object) playerModel.leftSleeve).setHidden();
                ((ModelPartBase) (Object) playerModel.rightSleeve).setHidden();
            }
        }
        if (livingEntity instanceof AbstractClientPlayer player && (Object) this instanceof PlayerModel<?> playerModel
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            ((ModelPartBase) (Object) playerModel.body).setHidden();
            ((ModelPartBase) (Object) ((PlayerModelAccess) this).getCloak()).setHidden();
        }
    }

}
