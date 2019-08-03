package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.FirstPersonRenderer;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.AbsoluteHand;
import net.minecraft.util.math.MathHelper;

@Mixin(FirstPersonRenderer.class)
public abstract class FirstPersonRendererMixin {


	@Inject(at = @At("HEAD"), method = "renderFirstPersonItem", cancellable = true)
	public void renderFirstPersonItem(float float_1, CallbackInfo info) {
		if(MinecraftClient.getInstance().player.getMainHandStack().getItem() == Items.FILLED_MAP && MinecraftClient.getInstance().player.getOffHandStack().isEmpty()) {
			//render normal map
			FirstPersonModelMod.hideArms = true;
		}else if(MinecraftClient.getInstance().player.getMainHandStack().getItem() == Items.FILLED_MAP){
			float float_3 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevPitch, MinecraftClient.getInstance().player.pitch);
			float float_4 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevYaw, MinecraftClient.getInstance().player.yaw);
			this.rotate(float_3, float_4);
			this.applyLightmap();
			this.applyCameraAngles(float_1);
			GlStateManager.enableRescaleNormal();
			FirstPersonModelMod.hideArms = true;
			this.renderMapInOneHand(0F, MinecraftClient.getInstance().player.getMainHand(), 1F, MinecraftClient.getInstance().player.getOffHandStack());
			GlStateManager.disableRescaleNormal();
			GuiLighting.disable();
			info.cancel();
		}else if(MinecraftClient.getInstance().player.getOffHandStack().getItem() == Items.FILLED_MAP){
			//render only offhand map
			float float_3 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevPitch, MinecraftClient.getInstance().player.pitch);
			float float_4 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevYaw, MinecraftClient.getInstance().player.yaw);
			this.rotate(float_3, float_4);
			this.applyLightmap();
			this.applyCameraAngles(float_1);
			GlStateManager.enableRescaleNormal();
			FirstPersonModelMod.hideArms = true;
			this.renderMapInOneHand(0F, MinecraftClient.getInstance().player.getMainHand().getOpposite(), 1F, MinecraftClient.getInstance().player.getOffHandStack());
			GlStateManager.disableRescaleNormal();
			GuiLighting.disable();
			info.cancel();
		} else {
			info.cancel();
		}

	}

	@Shadow
	abstract void renderMapInOneHand(float float_1, AbsoluteHand absoluteHand_1, float float_2, ItemStack itemStack_1);
	@Shadow
	abstract void rotate(float float_1, float float_2);
	@Shadow
	abstract void applyLightmap();
	@Shadow
	abstract void applyCameraAngles(float float_1);

}
