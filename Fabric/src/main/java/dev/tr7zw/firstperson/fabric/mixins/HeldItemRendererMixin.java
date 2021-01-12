package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.HeldItemBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

/**
 * Hides the normal first person hands and handels map rendering
 *
 */
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin implements HeldItemBase {

	@Shadow
	private EntityRenderDispatcher renderManager;
	@Shadow
	private MinecraftClient client;

	@Inject(at = @At("HEAD"), method = "renderFirstPersonItem", cancellable = true)
	public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
			float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		if (!skip()) {
			info.cancel();
			return;
		}
		if(!FirstPersonModelCore.config.firstPerson.doubleHands)return;
		boolean bl = hand == Hand.MAIN_HAND;
		Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
		matrices.push();
		if (item.isEmpty()) {
			if (!bl && !player.isInvisible()) {
				this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress,
						arm);
			}
		}
		matrices.pop();
	}

	@Shadow
	public abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
			float equipProgress, float swingProgress, Arm arm);
}
