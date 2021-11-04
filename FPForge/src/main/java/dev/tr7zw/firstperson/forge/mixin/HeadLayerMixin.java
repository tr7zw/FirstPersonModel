package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.mixinbase.HeadFeatureRendererBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.entity.LivingEntity;

// HeadFeatureRenderer equivelent
@Mixin(HeadLayer.class)
public abstract class HeadLayerMixin<T extends LivingEntity, M extends EntityModel<T> & IHasHead> extends LayerRenderer<T, M>
		implements HeadFeatureRendererBase {

	public HeadLayerMixin(IEntityRenderer<T, M> entityRendererIn) {
		super(entityRendererIn);
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch, CallbackInfo info) {
		process(info);
	}

}
