package de.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.FirstPersonRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonRenderer.class)
public abstract class FirstPersonRendererMixin {
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"), method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;F)V", cancellable = true)
	private void renderFirstPersonItem(AbstractClientPlayerEntity abstractClientPlayerEntity_1, float float_1, float float_2, Hand hand_1, float float_3, ItemStack itemStack_1, float float_4, CallbackInfo ci) {
		// Since we return early, we have to fix the GL matrix stack
		GlStateManager.popMatrix();
		ci.cancel();
	}
}
