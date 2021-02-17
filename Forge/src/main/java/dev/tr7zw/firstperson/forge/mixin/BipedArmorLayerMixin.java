package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

@Mixin(BipedArmorLayer.class)
public abstract class BipedArmorLayerMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {

	public BipedArmorLayerMixin(IEntityRenderer<T, M> entityRendererIn) {
		super(entityRendererIn);
	}

	private boolean hideShoulders = false;
	
	@Inject(method = "func_241739_a_", at = @At("HEAD"))
	 private void renderArmor(MatrixStack matrices, IRenderTypeBuffer p_241739_2_, T livingEntity, EquipmentSlotType equipmentSlot, int p_241739_5_, A p_241739_6_, CallbackInfo info) {
		if(equipmentSlot == EquipmentSlotType.CHEST && FirstPersonModelCore.isRenderingPlayer && FirstPersonModelCore.config.firstPerson.vanillaHands) {
			hideShoulders = true;
		}else {
			hideShoulders = false;
		}
	 }
	
	@Inject(method = "setModelSlotVisible", at = @At("RETURN"))
	protected void setModelSlotVisible(A bipedEntityModel, EquipmentSlotType slotIn, CallbackInfo info) {
		if(hideShoulders) {
			bipedEntityModel.bipedLeftArm.showModel = false;
			bipedEntityModel.bipedRightArm.showModel = false;
		}
	}
	
}
