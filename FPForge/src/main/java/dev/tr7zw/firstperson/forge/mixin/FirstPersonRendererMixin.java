package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.mixinbase.HeldItemBase;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

//HeldItemRenderer
@Mixin(FirstPersonRenderer.class)
public abstract class FirstPersonRendererMixin implements HeldItemBase {

	@Inject(at = @At(value = "HEAD", ordinal = 2), method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", cancellable = true)
	public void renderItemInFirstPerson(AbstractClientPlayerEntity player, float partialTicks, float pitch, Hand handIn,
			float swingProgress, ItemStack stack, float equippedProgress, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, CallbackInfo info) {
		if (!skip()) {
			info.cancel();
			return;
		}
		if (!FirstPersonModelCore.config.firstPerson.doubleHands)
			return;
		boolean bl = handIn == Hand.MAIN_HAND;
		HandSide arm = bl ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
		matrixStackIn.push();
		if (stack.isEmpty()) {
			if (!bl && !player.isInvisible()) {
				this.renderArmFirstPerson(matrixStackIn, bufferIn, combinedLightIn, equippedProgress, swingProgress,
						arm);
			}
		}
		matrixStackIn.pop();
	}

	@Shadow
	public abstract void renderArmFirstPerson(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, float equippedProgress, float swingProgress, HandSide side);

}
