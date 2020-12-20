package de.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.mixinbase.HeadFeatureRendererBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

/**
 * Stops items(not armor!, also blocks are usually not visible from the inside) on the head from rendering while in first person.
 *
 * @param <T>
 * @param <M>
 */
@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead>
		extends FeatureRenderer<T, M>  implements HeadFeatureRendererBase {

	public HeadFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	protected void render(MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, int int_1,
			T livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6,
			CallbackInfo info) {
		process(livingEntity_1, matrixStack_1, info);
	}

}
