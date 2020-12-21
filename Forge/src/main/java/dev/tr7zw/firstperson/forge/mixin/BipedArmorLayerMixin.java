package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.forge.FirstPersonModelMod;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

//ArmorFeature
@Mixin(BipedArmorLayer.class)
public abstract class BipedArmorLayerMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {

	public BipedArmorLayerMixin(IEntityRenderer<T, M> entityRendererIn) {
		super(entityRendererIn);
	}
	
	@Inject(at = @At("RETURN"), method = "setModelSlotVisible")
	protected void setModelSlotVisible(A bipedEntityModel_1, EquipmentSlotType slotIn, CallbackInfo info) {
		if (FirstPersonModelMod.hideNextHeadArmor && slotIn == EquipmentSlotType.HEAD) {
			FirstPersonModelMod.hideNextHeadArmor = false;
			bipedEntityModel_1.bipedHead.showModel = false;
			bipedEntityModel_1.bipedHeadwear.showModel = false;
		}
	}

}
