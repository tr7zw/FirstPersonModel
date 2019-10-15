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
		if(MinecraftClient.getInstance().player.getMainHandStack().getItem() == Items.FILLED_MAP){
			//render only offhand map
			//float float_3 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevPitch, MinecraftClient.getInstance().player.pitch);
			//float float_4 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevYaw, MinecraftClient.getInstance().player.yaw);
			//this.rotate(float_3, float_4);
			this.applyLightmap();
			//this.applyCameraAngles(float_1);
			GlStateManager.enableRescaleNormal();
			renderMapAsMinimap(MinecraftClient.getInstance().player.getMainHandStack());
			GlStateManager.disableRescaleNormal();
			GuiLighting.disable();
			info.cancel();
		}else if(MinecraftClient.getInstance().player.getOffHandStack().getItem() == Items.FILLED_MAP){
			//render only offhand map
			//float float_3 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevPitch, MinecraftClient.getInstance().player.pitch);
			//float float_4 = MathHelper.lerp(float_1, MinecraftClient.getInstance().player.prevYaw, MinecraftClient.getInstance().player.yaw);
			//this.rotate(float_3, float_4);
			this.applyLightmap();
			//this.applyCameraAngles(float_1);
			GlStateManager.enableRescaleNormal();
			renderMapAsMinimap(MinecraftClient.getInstance().player.getOffHandStack());
			GlStateManager.disableRescaleNormal();
			GuiLighting.disable();
			info.cancel();
		} else {
			info.cancel();
		}

	}
	
	public void renderMapAsMinimap(ItemStack item) {
		GlStateManager.pushMatrix();
		float size = (float)MinecraftClient.getInstance().getFramebuffer().viewWidth / (float)MinecraftClient.getInstance().getFramebuffer().viewHeight;
		GlStateManager.translatef(0, 0.0f, 0.0f); // 3rd arg is size
	      float float_3 =  1.0F;
	      GlStateManager.translatef(float_3 * 0.125F, -0.125F, 0.0F);

	      GlStateManager.pushMatrix();
	      GlStateManager.translatef(float_3 * 0.51F, -0.08F, -0.75F);
	      float float_5 = MathHelper.sin(3.1415927F);
	      float float_6 = -0.5F * float_5;
	      float float_7 = 0.4F * MathHelper.sin(6.2831855F);
	      float float_8 = -0.3F * MathHelper.sin(1 * 3.1415927F);
	      GlStateManager.translatef(float_3 * float_6, float_7 - 0.3F * float_5, float_8);
	      GlStateManager.rotatef(float_5 * -45.0F, 1.0F, 0.0F, 0.0F);
	      GlStateManager.rotatef(float_3 * float_5 * -30.0F, 0.0F, 1.0F, 0.0F);
	      GlStateManager.translatef(0.33f, 0.65f, -0.2f);
	      this.renderFirstPersonMap(item);
	      GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
	@Shadow
	abstract void renderFirstPersonMap(ItemStack itemStack_1);
	@Shadow
	abstract void renderMapInOneHand(float float_1, AbsoluteHand absoluteHand_1, float float_2, ItemStack itemStack_1);
	@Shadow
	abstract void rotate(float float_1, float float_2);
	@Shadow
	abstract void applyLightmap();
	@Shadow
	abstract void applyCameraAngles(float float_1);

}
