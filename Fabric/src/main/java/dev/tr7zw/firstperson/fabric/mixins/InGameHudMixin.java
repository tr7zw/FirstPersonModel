package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.config.PaperDollSettings.DollHeadMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

/**
 * Paper doll rendering
 *
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {

	private final MinecraftClient mc = MinecraftClient.getInstance();

	@Inject(at = @At("HEAD"), method = "render")
	public void render(MatrixStack matrixStack, float delta, CallbackInfo info) {
		if (FirstPersonModelCore.config.paperDoll.dollEnabled && !mc.options.debugEnabled) {
			int xpos = 0;
			int ypos = 0;
			switch (FirstPersonModelCore.config.paperDoll.location) {
			case TOP_LEFT:
				xpos = 25 + FirstPersonModelCore.config.paperDoll.dollXOffset;
				ypos = 55 + FirstPersonModelCore.config.paperDoll.dollYOffset;
				break;
			case TOP_RIGHT:
				xpos = mc.getWindow().getScaledWidth() - (25 + FirstPersonModelCore.config.paperDoll.dollXOffset);
				ypos = 55 + FirstPersonModelCore.config.paperDoll.dollYOffset;
				break;
			case BOTTOM_LEFT:
				xpos = 25 + FirstPersonModelCore.config.paperDoll.dollXOffset;
				ypos = mc.getWindow().getScaledHeight() - (55 + FirstPersonModelCore.config.paperDoll.dollYOffset);
				break;
			case BOTTOM_RIGHT:
				xpos = mc.getWindow().getScaledWidth() - (25 + FirstPersonModelCore.config.paperDoll.dollXOffset);
				ypos = mc.getWindow().getScaledHeight() - (55 + FirstPersonModelCore.config.paperDoll.dollYOffset);
				break;
			}
			int size = 25 + FirstPersonModelCore.config.paperDoll.dollSize;
			int lookSides = -FirstPersonModelCore.config.paperDoll.dollLookingSides;
			int lookUpDown = FirstPersonModelCore.config.paperDoll.dollLookingUpDown;
			if (mc.player.isFallFlying() || mc.player.isUsingRiptide()) {
				lookSides = 0;
				lookUpDown = 40;
			}
			LivingEntity playerEntity = mc.player;
			if (mc.getCameraEntity() != playerEntity && mc.getCameraEntity() instanceof LivingEntity) {
				playerEntity = (LivingEntity) mc.getCameraEntity();
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
        MatrixStack matrixStack1 = RenderSystem.getModelViewStack();
        matrixStack1.push();
        matrixStack1.translate((double) x, (double) y, 1050.0D);
        matrixStack1.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0D, 0.0D, 1000.0D);
        matrixStack2.scale((float) size, (float) size, (float) size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(l * 20.0F);
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);
		float m = livingEntity.bodyYaw;
		float renderYaw = livingEntity.getYaw();
		float prevRenderYaw = livingEntity.prevYaw;
		float prevBodyYaw = livingEntity.prevBodyYaw;
		float n = livingEntity.getYaw();
		float prevYaw = livingEntity.prevYaw;
		float o = livingEntity.getPitch();
		float prevPitch = livingEntity.prevPitch;
		float p = livingEntity.prevHeadYaw;
		float q = livingEntity.headYaw;
		Vec3d vel = livingEntity.getVelocity();
		livingEntity.bodyYaw = 180.0F + h * 20.0F;
		livingEntity.setYaw(180.0F + h * 40.0F);
		livingEntity.prevBodyYaw = livingEntity.bodyYaw;
		livingEntity.prevYaw = livingEntity.getYaw();
		livingEntity.setVelocity(Vec3d.ZERO);
		if (lockHead) {
			livingEntity.setPitch(-l * 20.0F);
			livingEntity.prevPitch = livingEntity.getPitch();
			livingEntity.headYaw = livingEntity.getYaw();
			livingEntity.prevHeadYaw = livingEntity.getYaw();
		} else {
			if (FirstPersonModelCore.config.paperDoll.dollHeadMode == DollHeadMode.FREE) {
				livingEntity.headYaw = 180.0F + h * 40.0F - (m - q);
				livingEntity.prevHeadYaw = 180.0F + h * 40.0F - (prevBodyYaw - p);
			} else {
				livingEntity.headYaw = 180.0F + h * 40.0F - (renderYaw - q);
				livingEntity.prevHeadYaw = 180.0F + h * 40.0F - (prevRenderYaw - p);
			}
		}
	    DiffuseLighting.method_34742();
		EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
		quaternion2.conjugate();
		entityRenderDispatcher.setRotation(quaternion2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
		// Mc renders the player in the inventory without delta, causing it to look
		// "laggy". Good luck unseeing this :)
		entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack2, immediate, 15728880);
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(true);
		livingEntity.bodyYaw = m;
		livingEntity.prevBodyYaw = prevBodyYaw;
		livingEntity.setYaw(n);
		livingEntity.prevYaw = prevYaw;
		livingEntity.setPitch(o);
		livingEntity.prevPitch = prevPitch;
		livingEntity.prevHeadYaw = p;
		livingEntity.headYaw = q;
		livingEntity.setVelocity(vel);
        matrixStack1.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
	}

}
