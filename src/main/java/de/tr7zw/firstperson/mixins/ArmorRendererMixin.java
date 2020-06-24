package de.tr7zw.firstperson.mixins;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
//import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorRendererMixin <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {


	public ArmorRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Inject(at = @At("RETURN"), method = "setVisible")
	protected void setVisible(A bipedEntityModel_1, EquipmentSlot equipmentSlot_1, CallbackInfo info) {
		if(FirstPersonModelMod.hideNextHeadArmor && equipmentSlot_1 == EquipmentSlot.HEAD) {
			FirstPersonModelMod.hideNextHeadArmor = false;
	         bipedEntityModel_1.head.visible = false;
	         bipedEntityModel_1.helmet.visible = false;
		}
	}

}
