package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.HeadFeatureRendererBase;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Stops items(not armor!, also blocks are usually not visible from the inside)
 * on the head from rendering while in first person.
 *
 * @param <T>
 * @param <M>
 */
@Mixin(CustomHeadLayer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
		extends RenderLayer<T, M> implements HeadFeatureRendererBase {

	public HeadFeatureRendererMixin(RenderLayerParent<T, M> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	protected void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l,
			CallbackInfo info) {
		if (FirstPersonModelCore.config.firstPerson.playerHeadSkins
				&& shouldHide(livingEntity.getItemBySlot(EquipmentSlot.HEAD)) && livingEntity instanceof Player) {
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
