package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import de.tr7zw.firstperson.mixinbase.HeldItemBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

//HeldItemRenderer
@Mixin(FirstPersonRenderer.class)
public abstract class FirstPersonRendererMixin implements HeldItemBase{

	private final Minecraft mc = Minecraft.getInstance();
	
	@Inject(at = @At(value = "HEAD", ordinal = 2), method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", cancellable = true)
	public void renderItemInFirstPerson(AbstractClientPlayerEntity player, float partialTicks, float pitch, Hand handIn, float swingProgress, ItemStack stack, float equippedProgress, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, CallbackInfo info) {
		if(skip())return;
		if(mc.player.getHeldItemMainhand().getItem() == Items.FILLED_MAP){
			GlStateManager.enableRescaleNormal();
			renderMapAsMinimap(matrixStackIn, bufferIn, combinedLightIn, mc.player.getHeldItemMainhand());
			GlStateManager.disableRescaleNormal();
			info.cancel();
		}else if(mc.player.getHeldItemOffhand().getItem() == Items.FILLED_MAP){
			GlStateManager.enableRescaleNormal();
			renderMapAsMinimap(matrixStackIn, bufferIn, combinedLightIn, mc.player.getHeldItemOffhand());
			GlStateManager.disableRescaleNormal();
			info.cancel();
		} else {
			info.cancel();
		}

	}
	
	public void renderMapAsMinimap(MatrixStack matrixStack_1, IRenderTypeBuffer vertexConsumerProvider_1, int combinedLightIn, ItemStack item) {
		GlStateManager.pushMatrix();
		//float size = (float)mc.getFramebuffer().viewportWidth / (float)mc.getFramebuffer().viewportHeight;
		GlStateManager.translatef(0, 0.0f, 0.0f); // 3rd arg is size
	      float float_3 =  1.0F;
	      GlStateManager.translatef(float_3 * 0.125F, -0.125F, 0.0F);
	      GlStateManager.translatef(float_3 * 0.51F, -0.08F, -0.75F);
	      float float_5 = MathHelper.sin(3.1415927F);
	      float float_6 = -0.5F * float_5;
	      float float_7 = 0.4F * MathHelper.sin(6.2831855F);
	      float float_8 = -0.3F * MathHelper.sin(1 * 3.1415927F);
	      GlStateManager.translatef(float_3 * float_6, float_7 - 0.3F * float_5, float_8);
	      GlStateManager.rotatef(float_5 * -45.0F, 1.0F, 0.0F, 0.0F);
	      GlStateManager.rotatef(float_3 * float_5 * -30.0F, 0.0F, 1.0F, 0.0F);
	      GlStateManager.translatef(0.33f, 0.65f, -0.2f);
	      this.renderMapFirstPerson(matrixStack_1, vertexConsumerProvider_1, combinedLightIn, item);
		GlStateManager.popMatrix();
	}
	
	@Shadow
	public abstract void renderMapFirstPerson(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, ItemStack stack);
	
}
