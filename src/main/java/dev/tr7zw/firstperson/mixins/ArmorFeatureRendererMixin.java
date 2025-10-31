package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.versionless.mixinbase.ModelPartBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
//? if >= 1.21.3 {

import net.minecraft.world.item.equipment.Equippable;
//? }

// lower prio to run before fabric api
@SuppressWarnings("rawtypes")
@Mixin(value = HumanoidArmorLayer.class, priority = 100)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends HumanoidModel, A extends HumanoidModel>
        extends RenderLayer {

    private static Minecraft fpmMcInstance = Minecraft.getInstance();
    private static boolean hideLeftArm = false;
    private static boolean hideRightArm = false;

    protected ArmorFeatureRendererMixin(RenderLayerParent context) {
        super(context);
    }

    /*//#if MC >= 12103
    @Inject(method = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z", at = @At("HEAD"), cancellable = true)
    private static void shouldRender(Equippable equippable, EquipmentSlot equipmentSlot,
            CallbackInfoReturnable<Boolean> ci) {
        hideLeftArm = false;
        hideRightArm = false;
        if (FirstPersonModelCore.instance.isRenderingPlayer()) {
            if (equipmentSlot == EquipmentSlot.HEAD) {
                ci.setReturnValue(false);
            }
            if (equipmentSlot == EquipmentSlot.CHEST) {
                if (FirstPersonModelCore.instance.getLogicHandler().isSwimming(Minecraft.getInstance().player)) {
                    ci.setReturnValue(false);
                }
                if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems()) {
                    hideLeftArm = true;
                    hideRightArm = true;
                } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO DYNAMIC HAND
    
                    if (!Minecraft.getInstance().player.getOffhandItem().isEmpty())
                        hideLeftArm = true;
                    if (!Minecraft.getInstance().player.getMainHandItem().isEmpty())
                        hideRightArm = true;
                }
            }
        }
    }
    
    @Inject(method = "setPartVisibility", at = @At("TAIL"))
    protected void setPartVisibility(A model, EquipmentSlot slot, CallbackInfo ci) {
        if (hideLeftArm) {
            ((ModelPartBase) (Object) model.leftArm).setHidden();
        } else {
            ((ModelPartBase) (Object) model.leftArm).showAgain();
        }
        if (hideRightArm) {
            ((ModelPartBase) (Object) model.rightArm).setHidden();
        } else {
            ((ModelPartBase) (Object) model.rightArm).showAgain();
        }
    }
    
    //? } else {
    
    // @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    //? }
    private void renderArmor(PoseStack matrices, MultiBufferSource vertexConsumers, T livingEntity,
            EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo info) {
        if (livingEntity != fpmMcInstance.cameraEntity) {
            return;
        }
        if (equipmentSlot == EquipmentSlot.HEAD && FirstPersonModelCore.instance.isRenderingPlayer()) {
            info.cancel();
        }
        if (equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.instance.isRenderingPlayer()
                && livingEntity instanceof LocalPlayer player
                && FirstPersonModelCore.instance.getLogicHandler().isSwimming(player)) {
            info.cancel();
        }
        if (equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.instance.isRenderingPlayer()) {
            if (FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems()) {
                ((ModelPartBase) (Object) bipedEntityModel.leftArm).setHidden();
                ((ModelPartBase) (Object) bipedEntityModel.rightArm).setHidden();
            } else if (FirstPersonModelCore.instance.getLogicHandler().dynamicHandsEnabled()) {// TODO DYNAMIC HAND
                if (!livingEntity.getOffhandItem().isEmpty())
                    ((ModelPartBase) (Object) bipedEntityModel.leftArm).setHidden();
                if (!livingEntity.getMainHandItem().isEmpty())
                    ((ModelPartBase) (Object) bipedEntityModel.rightArm).setHidden();
            } else {
                ((ModelPartBase) (Object) bipedEntityModel.leftArm).showAgain();
                ((ModelPartBase) (Object) bipedEntityModel.rightArm).showAgain();
            }
        } else {
            ((ModelPartBase) (Object) bipedEntityModel.leftArm).showAgain();
            ((ModelPartBase) (Object) bipedEntityModel.rightArm).showAgain();
        }
    }*/

}
