package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

//IngameHudMixin
@Mixin(IngameGui.class)
public class IngameGuiMixin {

	private final Minecraft mc = Minecraft.getInstance();
	
	@Inject(at = @At("HEAD"), method = "renderHotbar")
	public void renderHotbar(float partialTicks, MatrixStack matrixStack, CallbackInfo callback) {
		if (FirstPersonModelCore.config.paperDoll.dollEnabled && !mc.gameSettings.showDebugInfo) {
			int xpos = 25 + FirstPersonModelCore.config.paperDoll.dollXOffset;
			int ypos = 55 + FirstPersonModelCore.config.paperDoll.dollYOffset;
			int size = 25 + FirstPersonModelCore.config.paperDoll.dollSize;
			int lookSides = -FirstPersonModelCore.config.paperDoll.dollLookingSides;
			int lookUpDown = FirstPersonModelCore.config.paperDoll.dollLookingUpDown;
			if (mc.player.isElytraFlying() || mc.player.isSpinAttacking()) {
				lookSides = 0;
				lookUpDown = 40;
			}
			LivingEntity playerEntity = mc.player;
			if (mc.getRenderViewEntity() != playerEntity && mc.getRenderViewEntity() instanceof LivingEntity) {
				playerEntity = (LivingEntity) mc.getRenderViewEntity();
			}
			drawEntity(xpos, ypos, size, lookSides, lookUpDown, playerEntity, partialTicks,
					FirstPersonModelCore.config.paperDoll.dollLockedHead);
		}
	}
	
	// Modified version from InventoryScreen
	@SuppressWarnings({ "deprecation" })
	private void drawEntity(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity,
			float delta, boolean lockHead) {
		float h = (float) Math.atan((double) (mouseX / 40.0F));
		float l = (float) Math.atan((double) (mouseY / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) scale, (float) scale, (float) scale);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(l * 20.0F);
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float yaw = livingEntity.rotationYaw;
		//float prevBodyYaw = livingEntity.prevRenderYawOffset;
		//float n = livingEntity.rotationYaw;
		float prevYaw = livingEntity.prevRotationYaw;
		float pitch = livingEntity.rotationPitch;
		float prevPitch = livingEntity.prevRotationPitch;
		float prevHeadYaw = livingEntity.prevRotationYawHead;
		float headYaw = livingEntity.rotationYawHead;
		livingEntity.renderYawOffset = 180.0F + h * 20.0F;
		livingEntity.rotationYawHead = 180.0F + h * 40.0F;
		livingEntity.prevRenderYawOffset = livingEntity.renderYawOffset;
		livingEntity.prevRotationYawHead = livingEntity.rotationYawHead;
		if (lockHead) {
			livingEntity.rotationPitch = -l * 20.0F;
			livingEntity.prevRotationPitch = livingEntity.rotationPitch;
			livingEntity.rotationYawHead = livingEntity.renderYawOffset;
			livingEntity.prevRotationYawHead = livingEntity.renderYawOffset;
		} else {
			livingEntity.rotationYawHead = 180.0F + h * 40.0F - (yaw - headYaw);
			livingEntity.prevRotationYawHead = 180.0F + h * 40.0F - (prevYaw - prevHeadYaw);
		}
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers()
				.getBufferSource();
		// Mc renders the player in the inventory without delta, causing it to look
		// "laggy". Good luck unseeing this :)
		RenderSystem.runAsFancy(() -> {
			entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixstack,
					irendertypebuffer$impl, 15728880);
		});
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		livingEntity.renderYawOffset = yaw;
		livingEntity.prevRenderYawOffset = prevYaw;
		livingEntity.rotationYaw = yaw;
		livingEntity.prevRotationYaw = prevYaw;
		livingEntity.rotationPitch = pitch;
		livingEntity.prevRotationPitch = prevPitch;
		livingEntity.prevRotationYawHead = prevHeadYaw;
		livingEntity.rotationYawHead = headYaw;
		RenderSystem.popMatrix();
	}
	
}
