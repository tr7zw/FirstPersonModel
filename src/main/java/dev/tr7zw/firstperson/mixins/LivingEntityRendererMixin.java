package dev.tr7zw.firstperson.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.AgeableListModelAccess;
import dev.tr7zw.firstperson.access.PlayerAccess;
import dev.tr7zw.firstperson.access.PlayerModelAccess;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import dev.tr7zw.util.NMSHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    // pull all registers to try to get rid of the head or other bodyparts
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = Shift.AFTER), cancellable = true)
    public void renderPostAnim(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        if (!FirstPersonModelCore.instance.isRenderingPlayer())
            return;
        if (livingEntity instanceof Shulker) {
            return;// No need to mess with
        }
        Model model = getModel();
        boolean headShouldBeHidden = false;
        if (model instanceof AgeableListModelAccess agable) {
            agable.firstPersonHeadPartsGetter().forEach(part -> ((ModelPartBase) (Object) part).setHidden());
            headShouldBeHidden = true;
        }
        if (model instanceof HeadedModel headed) {
            ((ModelPartBase) (Object) headed.getHead()).setHidden();
            headShouldBeHidden = true;
        }
        if (model instanceof HumanoidModel<?> humanModel && livingEntity instanceof PlayerAccess playerAccess) {
            if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(livingEntity)) {
                ((ModelPartBase) (Object) humanModel.leftArm).setHidden();
                ((ModelPartBase) (Object) humanModel.rightArm).setHidden();
            } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                               // ITEM
                float offset = Mth.clamp(-NMSHelper.getXRot(Minecraft.getInstance().player) / 20 + 2, -0.0f, 0.7f);
                humanModel.rightArm.xRot += offset;
                humanModel.leftArm.xRot += offset;
//                humanModel.rightArm.offsetRotation(new Vector3f(offset, 0, 0));
//                humanModel.leftArm.offsetRotation(new Vector3f(offset, 0, 0));

                if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {// TODO DYNAMIC HAND
                    if (!playerAccess.getInventory().offhand.get(0).isEmpty()
                            || livingEntity.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
                        ((ModelPartBase) (Object) humanModel.leftArm).setHidden();
                    }
                    if (!playerAccess.getInventory().getSelected().isEmpty()) {
                        ((ModelPartBase) (Object) humanModel.rightArm).setHidden();
                    }
                }
            }
        }
        if (model instanceof VillagerHeadModel villaterHead) {
            villaterHead.hatVisible(false);
        }
        if (model instanceof PlayerModel<?> playerModel) {
            headShouldBeHidden = true;
            ((ModelPartBase) (Object) playerModel.hat).setHidden();
            if (livingEntity instanceof PlayerAccess playerAccess) {
                if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(livingEntity)) {
                    ((ModelPartBase) (Object) playerModel.leftSleeve).setHidden();
                    ((ModelPartBase) (Object) playerModel.rightSleeve).setHidden();
                } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                                   // ITEM
                    float offset = Mth.clamp(-NMSHelper.getXRot(Minecraft.getInstance().player) / 20 + 2, -0.0f, 0.7f);
                    playerModel.rightSleeve.xRot += offset;
                    playerModel.leftSleeve.xRot += offset;
//                    playerModel.rightSleeve.offsetRotation(new Vector3f(offset, 0, 0));
//                    playerModel.leftSleeve.offsetRotation(new Vector3f(offset, 0, 0));

                    if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {// TODO DYNAMIC HAND
                        if (!playerAccess.getInventory().offhand.get(0).isEmpty()
                                || livingEntity.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
                            ((ModelPartBase) (Object) playerModel.leftSleeve).setHidden();
                        }
                        if (!playerAccess.getInventory().getSelected().isEmpty()) {
                            ((ModelPartBase) (Object) playerModel.rightSleeve).setHidden();
                        }
                    }
                }
            }
        }
        if (livingEntity instanceof AbstractClientPlayer player && (Object) model instanceof PlayerModel<?> playerModel
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            ((ModelPartBase) (Object) playerModel.body).setHidden();
            ((ModelPartBase) (Object) ((PlayerModelAccess) model).getCloak()).setHidden();
        }
        if (!headShouldBeHidden) {
            // we failed to hide the head. So either its a mob without one, or something is
            // going really wrong. Cancel the render for the firstperson mode
            matrixStack.popPose();
            info.cancel();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void renderReturn(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        if (!FirstPersonModelCore.instance.isRenderingPlayer())
            return;
        // revert sate
        Model model = getModel();
        if (model instanceof AgeableListModelAccess agable) {
            agable.firstPersonHeadPartsGetter().forEach(part -> ((ModelPartBase) (Object) part).showAgain());
        }
        if (model instanceof HeadedModel headed) {
            ((ModelPartBase) (Object) headed.getHead()).showAgain();
        }
        if (model instanceof HumanoidModel<?> humanModel) {
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()
                    && !FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
                ((ModelPartBase) (Object) humanModel.leftArm).showAgain();
                ((ModelPartBase) (Object) humanModel.rightArm).showAgain();
            } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                               // ITEM
                if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {// TODO DYNAMIC HAND
                    ((ModelPartBase) (Object) humanModel.leftArm).showAgain();
                    ((ModelPartBase) (Object) humanModel.rightArm).showAgain();
                } else {
                    if (!livingEntity.getOffhandItem().isEmpty())
                        ((ModelPartBase) (Object) humanModel.leftArm).showAgain();
                    if (!livingEntity.getMainHandItem().isEmpty())
                        ((ModelPartBase) (Object) humanModel.rightArm).showAgain();
                }
            }
        }
        if (model instanceof VillagerHeadModel villaterHead) {
            villaterHead.hatVisible(false);
        }
        if (model instanceof PlayerModel<?> playerModel) {
            ((ModelPartBase) (Object) playerModel.hat).showAgain();
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands()) {
                ((ModelPartBase) (Object) playerModel.leftSleeve).showAgain();
                ((ModelPartBase) (Object) playerModel.rightSleeve).showAgain();
            } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                               // ITEM
                if (!livingEntity.getOffhandItem().isEmpty())
                    ((ModelPartBase) (Object) playerModel.leftSleeve).showAgain();
                if (!livingEntity.getMainHandItem().isEmpty())
                    ((ModelPartBase) (Object) playerModel.rightSleeve).showAgain();
            } else {
                ((ModelPartBase) (Object) playerModel.leftSleeve).showAgain();
                ((ModelPartBase) (Object) playerModel.rightSleeve).showAgain();
            }
        }
        if ((Object) model instanceof PlayerModel<?> playerModel) {
            ((ModelPartBase) (Object) playerModel.body).showAgain();
            ((ModelPartBase) (Object) ((PlayerModelAccess) model).getCloak()).showAgain();
        }
        FirstPersonModelCore.instance.setRenderingPlayer(false);
    }

    @Shadow
    public abstract EntityModel<LivingEntity> getModel();

}
