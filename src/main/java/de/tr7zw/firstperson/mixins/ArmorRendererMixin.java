package de.tr7zw.firstperson.mixins;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorBipedFeatureRenderer.class)
public abstract class ArmorRendererMixin <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends ArmorFeatureRenderer<T, M, A>  {

	protected ArmorRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1, A bipedEntityModel_1,
			A bipedEntityModel_2) {
		super(featureRendererContext_1, bipedEntityModel_1, bipedEntityModel_2);
	}

	@Inject(at = @At("RETURN"), method = "method_4170")
	protected void method_4170(A bipedEntityModel_1, EquipmentSlot var2, CallbackInfo info) {
		if(FirstPersonModelMod.renderingFirstPersonPlayer && var2 == EquipmentSlot.HEAD) {
	         bipedEntityModel_1.head.visible = false;
	         bipedEntityModel_1.headwear.visible = false;
		}
	}

}
