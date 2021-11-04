package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.config.PaperDollSettings.DollHeadMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Paper doll rendering
 *
 */
@Mixin(Gui.class)
public class InGameHudMixin {

	private final Minecraft fpm_mc = Minecraft.getInstance();

	@Inject(at = @At("HEAD"), method = "render")
	public void render(PoseStack matrixStack, float delta, CallbackInfo info) {
		if (FirstPersonModelCore.config.paperDoll.dollEnabled && !fpm_mc.options.renderDebug) {
			int xpos = 0;
			int ypos = 0;
			switch (FirstPersonModelCore.config.paperDoll.location) {
			case TOP_LEFT:
				xpos = 25 + FirstPersonModelCore.config.paperDoll.dollXOffset;
				ypos = 55 + FirstPersonModelCore.config.paperDoll.dollYOffset;
				break;
			case TOP_RIGHT:
				xpos = fpm_mc.getWindow().getGuiScaledWidth() - (25 + FirstPersonModelCore.config.paperDoll.dollXOffset);
				ypos = 55 + FirstPersonModelCore.config.paperDoll.dollYOffset;
				break;
			case BOTTOM_LEFT:
				xpos = 25 + FirstPersonModelCore.config.paperDoll.dollXOffset;
				ypos = fpm_mc.getWindow().getGuiScaledHeight() - (55 + FirstPersonModelCore.config.paperDoll.dollYOffset);
				break;
			case BOTTOM_RIGHT:
				xpos = fpm_mc.getWindow().getGuiScaledWidth() - (25 + FirstPersonModelCore.config.paperDoll.dollXOffset);
				ypos = fpm_mc.getWindow().getGuiScaledHeight() - (55 + FirstPersonModelCore.config.paperDoll.dollYOffset);
				break;
			}
			int size = 25 + FirstPersonModelCore.config.paperDoll.dollSize;
			int lookSides = -FirstPersonModelCore.config.paperDoll.dollLookingSides;
			int lookUpDown = FirstPersonModelCore.config.paperDoll.dollLookingUpDown;
			if (fpm_mc.player.isFallFlying() || fpm_mc.player.isAutoSpinAttack()) {
				lookSides = 0;
				lookUpDown = 40;
			}
			LivingEntity playerEntity = fpm_mc.player;
			if (fpm_mc.getCameraEntity() != playerEntity && fpm_mc.getCameraEntity() instanceof LivingEntity) {
				playerEntity = (LivingEntity) fpm_mc.getCameraEntity();
			}
			drawEntity(xpos, ypos, size, lookSides, lookUpDown, playerEntity, delta,
					FirstPersonModelCore.config.paperDoll.dollHeadMode == DollHeadMode.LOCKED);
		}
	}

	// Modified version from InventoryScreen
	private void drawEntity(int x, int y, int size, float f, float g, LivingEntity livingEntity, float delta,
			boolean lockHead) {
		float h = (float) Math.atan((double) (f / 40.0F));
		float l = (float) Math.atan((double) (g / 40.0F));
        PoseStack matrixStack1 = RenderSystem.getModelViewStack();
        matrixStack1.pushPose();
        matrixStack1.translate((double) x, (double) y, 1050.0D);
        matrixStack1.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack matrixStack2 = new PoseStack();
        matrixStack2.translate(0.0D, 0.0D, 1000.0D);
        matrixStack2.scale((float) size, (float) size, (float) size);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(l * 20.0F);
        quaternion.mul(quaternion2);
        matrixStack2.mulPose(quaternion);
		float m = livingEntity.yBodyRot;
		float renderYaw = livingEntity.getYRot();
		float prevRenderYaw = livingEntity.yRotO;
		float prevBodyYaw = livingEntity.yBodyRotO;
		float n = livingEntity.getYRot();
		float prevYaw = livingEntity.yRotO;
		float o = livingEntity.getXRot();
		float prevPitch = livingEntity.xRotO;
		float p = livingEntity.yHeadRotO;
		float q = livingEntity.yHeadRot;
		Vec3 vel = livingEntity.getDeltaMovement();
		livingEntity.yBodyRot = 180.0F + h * 20.0F;
		livingEntity.setYRot(180.0F + h * 40.0F);
		livingEntity.yBodyRotO = livingEntity.yBodyRot;
		livingEntity.yRotO = livingEntity.getYRot();
		livingEntity.setDeltaMovement(Vec3.ZERO);
		if (lockHead) {
			livingEntity.setXRot(-l * 20.0F);
			livingEntity.xRotO = livingEntity.getXRot();
			livingEntity.yHeadRot = livingEntity.getYRot();
			livingEntity.yHeadRotO = livingEntity.getYRot();
		} else {
			if (FirstPersonModelCore.config.paperDoll.dollHeadMode == DollHeadMode.FREE) {
				livingEntity.yHeadRot = 180.0F + h * 40.0F - (m - q);
				livingEntity.yHeadRotO = 180.0F + h * 40.0F - (prevBodyYaw - p);
			} else {
				livingEntity.yHeadRot = 180.0F + h * 40.0F - (renderYaw - q);
				livingEntity.yHeadRotO = 180.0F + h * 40.0F - (prevRenderYaw - p);
			}
		}
	    Lighting.setupForEntityInInventory();
		EntityRenderDispatcher entityRenderDispatcher = fpm_mc.getEntityRenderDispatcher();
		quaternion2.conj();
		entityRenderDispatcher.overrideCameraOrientation(quaternion2);
		entityRenderDispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource immediate = fpm_mc.renderBuffers().bufferSource();
		// Mc renders the player in the inventory without delta, causing it to look
		// "laggy". Good luck unseeing this :)
		entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack2, immediate, 15728880);
		immediate.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		livingEntity.yBodyRot = m;
		livingEntity.yBodyRotO = prevBodyYaw;
		livingEntity.setYRot(n);
		livingEntity.yRotO = prevYaw;
		livingEntity.setXRot(o);
		livingEntity.xRotO = prevPitch;
		livingEntity.yHeadRotO = p;
		livingEntity.yHeadRot = q;
		livingEntity.setDeltaMovement(vel);
        matrixStack1.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
	}

}
