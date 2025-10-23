package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.access.LivingEntityRenderStateAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.InventoryUtil;
import dev.tr7zw.firstperson.access.AgeableListModelAccess;
//#if MC < 12103
//$$ import dev.tr7zw.firstperson.access.PlayerModelAccess;
//#endif
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import dev.tr7zw.transition.mc.EntityUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
//#if MC >= 12103
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif
//#if MC < 12104
//$$import net.minecraft.client.model.VillagerHeadModel;
//#endif

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    private static List<Runnable> revert = new ArrayList<Runnable>();

    /*// pull all registers to try to get rid of the head or other bodyparts
    //#if MC >= 12103
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V", shift = Shift.AFTER), cancellable = true)
    public void render(LivingEntityRenderState livingEntityRenderState, PoseStack matrixStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        if (!FirstPersonModelCore.instance.isRenderingPlayer())
            return;
        Entity entity = Minecraft.getInstance().cameraEntity;
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        //#else
        //$$@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = Shift.AFTER), cancellable = true)
        //$$public void renderPostAnim(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
        //$$        MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        //#endif
        if (!revert.isEmpty()) {
            for (Runnable r : revert) {
                r.run();
            }
            revert.clear();
        }
        if (!FirstPersonModelCore.instance.isRenderingPlayer())
            return;
        if (livingEntity instanceof Shulker) {
            return;// No need to mess with
        }
        Model model = getModel();
        boolean headShouldBeHidden = false;
        if (model instanceof AgeableListModelAccess agable) {
            agable.firstPersonHeadPartsGetter().forEach(part -> {
                ((ModelPartBase) (Object) part).setHidden();
                revert.add(() -> ((ModelPartBase) (Object) part).showAgain());
            });
            headShouldBeHidden = true;
        }
        if (model instanceof HeadedModel headed) {
            ((ModelPartBase) (Object) headed.getHead()).setHidden();
            revert.add(() -> ((ModelPartBase) (Object) headed.getHead()).showAgain());
            headShouldBeHidden = true;
        }
        if (model instanceof HumanoidModel<?> humanModel && livingEntity instanceof Player player) {
            if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(livingEntity)) {
                ((ModelPartBase) (Object) humanModel.leftArm).setHidden();
                ((ModelPartBase) (Object) humanModel.rightArm).setHidden();
                revert.add(() -> {
                    ((ModelPartBase) (Object) humanModel.leftArm).showAgain();
                    ((ModelPartBase) (Object) humanModel.rightArm).showAgain();
                });
            } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                               // ITEM
                float offset = Mth.clamp(-EntityUtil.getXRot(Minecraft.getInstance().player) / 20 + 2, -0.0f, 0.7f);
                humanModel.rightArm.xRot += offset;
                humanModel.leftArm.xRot += offset;
                //                humanModel.rightArm.offsetRotation(new Vector3f(offset, 0, 0));
                //                humanModel.leftArm.offsetRotation(new Vector3f(offset, 0, 0));

                if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {// TODO DYNAMIC HAND
                    if (!InventoryUtil.getOffhand(InventoryUtil.getInventory(player)).isEmpty()
                            || livingEntity.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
                        ((ModelPartBase) (Object) humanModel.leftArm).setHidden();
                        revert.add(() -> {
                            ((ModelPartBase) (Object) humanModel.leftArm).showAgain();
                        });
                    }
                    if (!InventoryUtil.getSelected(InventoryUtil.getInventory(player)).isEmpty()) {
                        ((ModelPartBase) (Object) humanModel.rightArm).setHidden();
                        revert.add(() -> {
                            ((ModelPartBase) (Object) humanModel.rightArm).showAgain();
                        });
                    }
                }
            }
        }
        //#if MC < 12104
        //$$if (model instanceof VillagerHeadModel villaterHead) {
        //$$    villaterHead.hatVisible(false);
        //$$    revert.add(() -> {
        //$$        villaterHead.hatVisible(true);
        //$$    });
        //$$}
        //#endif
        if (model instanceof PlayerModel playerModel) {
            headShouldBeHidden = true;
            ((ModelPartBase) (Object) playerModel.hat).setHidden();
            revert.add(() -> ((ModelPartBase) (Object) playerModel.hat).showAgain());
            if (livingEntity instanceof Player player) {
                if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(livingEntity)) {
                    ((ModelPartBase) (Object) playerModel.leftSleeve).setHidden();
                    ((ModelPartBase) (Object) playerModel.rightSleeve).setHidden();
                    revert.add(() -> {
                        ((ModelPartBase) (Object) playerModel.leftSleeve).showAgain();
                        ((ModelPartBase) (Object) playerModel.rightSleeve).showAgain();
                    });
                } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO VANILLA HANDS
                                                                                                   // ITEM
                    float offset = Mth.clamp(-EntityUtil.getXRot(Minecraft.getInstance().player) / 20 + 2, -0.0f, 0.7f);
                    playerModel.rightSleeve.xRot += offset;
                    playerModel.leftSleeve.xRot += offset;
                    //                    playerModel.rightSleeve.offsetRotation(new Vector3f(offset, 0, 0));
                    //                    playerModel.leftSleeve.offsetRotation(new Vector3f(offset, 0, 0));

                    if (!FirstPersonModelCore.instance.getLogicHandler().lookingDown()) {// TODO DYNAMIC HAND
                        if (!InventoryUtil.getOffhand(InventoryUtil.getInventory(player)).isEmpty()
                                || livingEntity.getMainHandItem().getItem().equals(Items.FILLED_MAP)) {
                            ((ModelPartBase) (Object) playerModel.leftSleeve).setHidden();
                            revert.add(() -> ((ModelPartBase) (Object) playerModel.leftSleeve).showAgain());
                        }
                        if (!InventoryUtil.getSelected(InventoryUtil.getInventory(player)).isEmpty()) {
                            ((ModelPartBase) (Object) playerModel.rightSleeve).setHidden();
                            revert.add(() -> ((ModelPartBase) (Object) playerModel.rightSleeve).showAgain());
                        }
                    }
                }
            }
        }
        if (livingEntity instanceof AbstractClientPlayer player && (Object) model instanceof PlayerModel playerModel
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            ((ModelPartBase) (Object) playerModel.body).setHidden();
            //#if MC >= 12103
            if (livingEntityRenderState instanceof PlayerRenderState prs) {
                prs.showCape = false;
            }
            //#else
            //$$((ModelPartBase) (Object) ((PlayerModelAccess) model).getCloak()).setHidden();
            //#endif
            revert.add(() -> {
                ((ModelPartBase) (Object) playerModel.body).showAgain();
                //#if MC < 12103
                //$$ ((ModelPartBase) (Object) ((PlayerModelAccess) model).getCloak()).showAgain();
                //#endif
            });
        }
        if (!headShouldBeHidden) {
            // we failed to hide the head. So either its a mob without one, or something is
            // going really wrong. Cancel the render for the firstperson mode
            matrixStack.popPose();
            info.cancel();
        }
    }*/

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void checkIfCameraEntity(LivingEntity livingEntity, LivingEntityRenderState livingEntityRenderState, float f, CallbackInfo ci) {
        ((LivingEntityRenderStateAccess) livingEntityRenderState).setIsCameraEntity(FirstPersonModelCore.instance.isRenderingPlayer());
    }

    /*@Inject(method = "render", at = @At("RETURN"))
    //#if MC >= 12103
    public void renderEnd(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        //#else
        //$$    public void renderReturn(LivingEntity livingEntity, float f, float g, PoseStack matrixStack,
        //$$        MultiBufferSource vertexConsumerProvider, int i, CallbackInfo info) {
        //#endifs
        if (!revert.isEmpty()) {
            for (Runnable r : revert) {
                r.run();
            }
            revert.clear();
        }
        FirstPersonModelCore.instance.setRenderingPlayer(false);
    }*/

    @Shadow
    public abstract EntityModel getModel();

}
