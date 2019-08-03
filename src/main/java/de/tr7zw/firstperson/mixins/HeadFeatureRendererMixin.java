package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> extends FeatureRenderer<T, M>  {

	public HeadFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1) {
		super(featureRendererContext_1);
		// TODO Auto-generated constructor stub
	}

	@Inject(at = @At("HEAD"), method = "method_17159", cancellable = true)
	protected void method_17159(T livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7, CallbackInfo info) {
		if(FirstPersonModelMod.hideNextHeadItem) {
			FirstPersonModelMod.hideNextHeadItem = false;
	        info.cancel();
		}
	}

}
