package de.tr7zw.firstperson;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.options.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class StaticMixinMethods {

	public static boolean isThirdPerson(boolean thirdPerson) {

		if(MinecraftClient.getInstance().player.isUsingRiptide())return false;
		if(MinecraftClient.getInstance().player.isFallFlying())return false;
		if(MinecraftClient.getInstance().player.isSpectator())return false;
		if(!FirstPersonModelMod.enabled || thirdPerson)return false;
		return true;
	}

	public static Vec3d offset;

	public static Vec3d getPositionOffset(AbstractClientPlayerEntity var1, Vec3d defValue, MatrixStack matrices) {
		if(var1 == MinecraftClient.getInstance().getCameraEntity() && MinecraftClient.getInstance().player.isSleeping() || !FirstPersonModelMod.fixBodyShadow(matrices))return defValue;
		double x,y,z = x = y = z = 0;
		AbstractClientPlayerEntity abstractClientPlayerEntity_1;
		double realYaw;
		if(var1 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON && FirstPersonModelMod.isRenderingPlayer) {
			abstractClientPlayerEntity_1 = (AbstractClientPlayerEntity) var1;
			realYaw = MathHelper.lerpAngleDegrees(MinecraftClient.getInstance().getTickDelta(), abstractClientPlayerEntity_1.prevYaw, abstractClientPlayerEntity_1.yaw);
			FirstPersonModelMod.isRenderingPlayer = false;
		}else {
			abstractClientPlayerEntity_1 = null;
			return defValue;
		}
		if (!abstractClientPlayerEntity_1.isMainPlayer() || MinecraftClient.getInstance().getCameraEntity() == abstractClientPlayerEntity_1) {
			float bodyOffset;
			if(abstractClientPlayerEntity_1.isSneaking()){
				bodyOffset = FirstPersonModelMod.sneakBodyOffset + (FirstPersonModelMod.config.sneakXOffset / 100f);
			}else if(MinecraftClient.getInstance().player.isInSwimmingPose()) {
				abstractClientPlayerEntity_1.bodyYaw = abstractClientPlayerEntity_1.headYaw;
				if(abstractClientPlayerEntity_1.prevPitch > 0) {
					bodyOffset = FirstPersonModelMod.swimUpBodyOffset;
				}else {
					bodyOffset = FirstPersonModelMod.swimDownBodyOffset;
				}
			}else if(abstractClientPlayerEntity_1.hasVehicle()) {
				bodyOffset = FirstPersonModelMod.inVehicleBodyOffset;
			}else{
				bodyOffset = 0.25f + (FirstPersonModelMod.config.xOffset / 100f);
			}
			x += bodyOffset * Math.sin(Math.toRadians(realYaw));
			z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
			if(MinecraftClient.getInstance().player.isInSwimmingPose()) {
				if(abstractClientPlayerEntity_1.prevPitch > 0  && abstractClientPlayerEntity_1.isSubmergedInWater()) {
					y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}else {
					y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}
			}

		}
		Vec3d vec = new Vec3d(x, y, z);
		abstractClientPlayerEntity_1 = null;
		FirstPersonModelMod.isRenderingPlayer = false;
		return vec;
	}

}