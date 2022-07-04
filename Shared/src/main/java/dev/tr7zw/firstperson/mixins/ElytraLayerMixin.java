package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
*/
@Mixin(ElytraLayer.class)
public class ElytraLayerMixin<T extends LivingEntity> {

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (FirstPersonModelCore.isRenderingPlayer && livingEntity instanceof LocalPlayer player && player.isSwimming()) {
			ci.cancel();
		}
	}

}
