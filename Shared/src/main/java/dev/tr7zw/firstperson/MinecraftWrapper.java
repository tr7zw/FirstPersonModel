package dev.tr7zw.firstperson;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.phys.Vec3;

public class MinecraftWrapper {

	private final Minecraft client;
	private Vec3 offset = Vec3.ZERO; //Current offset used for rendering
	
	public MinecraftWrapper(Minecraft instance) {
		this.client = instance;
	}

	public boolean applyThirdPerson(boolean thirdPerson) {
		if(client.player.isAutoSpinAttack())return false;
		if(client.player.isFallFlying())return false;
		if(client.player.getSwimAmount(1f) != 0 && !client.player.isVisuallySwimming())return false;
		if(!FirstPersonModelCore.enabled || thirdPerson)return false;
		return true;
	}
	
	public void updatePositionOffset(Entity player, Vec3 defValue) {
		if(player == client.getCameraEntity() && client.player.isSleeping()) {
			offset = defValue;
			return;
		}
		double x,y,z = x = y = z = 0;
		AbstractClientPlayer abstractClientPlayerEntity_1;
		double realYaw;
		if(player == client.player && client.options.getCameraType() == CameraType.FIRST_PERSON && FirstPersonModelCore.isRenderingPlayer) {
			abstractClientPlayerEntity_1 = (AbstractClientPlayer) player;
			realYaw = Mth.rotLerp(client.getFrameTime(), abstractClientPlayerEntity_1.yBodyRotO, abstractClientPlayerEntity_1.yBodyRot);
		}else {
			offset = defValue;
			return;
		}
		if (!abstractClientPlayerEntity_1.isLocalPlayer() || client.getCameraEntity() == abstractClientPlayerEntity_1) {
			float bodyOffset;
			if(client.player.isVisuallySwimming()) {
				abstractClientPlayerEntity_1.yBodyRot = abstractClientPlayerEntity_1.yHeadRot;
				if(abstractClientPlayerEntity_1.xRotO > 0) {
					bodyOffset = FirstPersonModelCore.swimUpBodyOffset;
				}else {
					bodyOffset = FirstPersonModelCore.swimDownBodyOffset;
				}
			}else if(abstractClientPlayerEntity_1.isShiftKeyDown()){
				bodyOffset = FirstPersonModelCore.sneakBodyOffset + (FirstPersonModelCore.config.sneakXOffset / 100f);
			}else if(abstractClientPlayerEntity_1.isPassenger()) {
				if(abstractClientPlayerEntity_1.getVehicle() instanceof Boat || abstractClientPlayerEntity_1.getVehicle() instanceof Minecart) {
					realYaw = Mth.rotLerp(client.getFrameTime(), abstractClientPlayerEntity_1.yBodyRotO, abstractClientPlayerEntity_1.yBodyRot);
				} else if(abstractClientPlayerEntity_1.getVehicle() instanceof LivingEntity){
					realYaw = Mth.rotLerp(client.getFrameTime(), ((LivingEntity)abstractClientPlayerEntity_1.getVehicle()).yBodyRotO, ((LivingEntity)abstractClientPlayerEntity_1.getVehicle()).yBodyRot);
				} else {
				}
				bodyOffset = FirstPersonModelCore.inVehicleBodyOffset + (FirstPersonModelCore.config.sitXOffset / 100f);
			}else{
				bodyOffset = 0.25f + (FirstPersonModelCore.config.xOffset / 100f);
			}
			x += bodyOffset * Math.sin(Math.toRadians(realYaw));
			z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
			if(client.player.isVisuallySwimming()) {
				if(abstractClientPlayerEntity_1.xRotO > 0  && abstractClientPlayerEntity_1.isUnderWater()) {
					y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
				}else {
					y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.xRotO));
				}
			}

		}
		Vec3 vec = new Vec3(x, y, z);
		abstractClientPlayerEntity_1 = null;
		offset = vec;
	}

	
	public Vec3 getOffset() {
		return offset;
	}

}
