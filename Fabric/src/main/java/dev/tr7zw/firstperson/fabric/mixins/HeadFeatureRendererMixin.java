package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.HeadFeatureRendererBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Stops items(not armor!, also blocks are usually not visible from the inside)
 * on the head from rendering while in first person.
 *
 * @param <T>
 * @param <M>
 */
@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead>
		extends FeatureRenderer<T, M> implements HeadFeatureRendererBase {

	public HeadFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	protected void render(MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, int int_1,
			T livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6,
			CallbackInfo info) {
		if (FirstPersonModelCore.config.firstPerson.playerHeadSkins
				&& shouldHide(livingEntity_1.getEquippedStack(EquipmentSlot.HEAD)) && livingEntity_1 instanceof PlayerEntity) {
			info.cancel();
			return;
		}
		process(info);
	}

	private boolean shouldHide(ItemStack item) {
		if (item.isEmpty())
			return false;
		if (item.getItem() == Items.PLAYER_HEAD)
			return true;
		return false;
	}

}
