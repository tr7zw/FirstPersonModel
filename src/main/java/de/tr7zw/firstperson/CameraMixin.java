package de.tr7zw.firstperson;

import net.minecraft.client.MinecraftClient;

public class CameraMixin {

	public static boolean isThirdPerson(boolean thirdPerson) {
		//KosmX - debug optifine
		System.out.println("check FP");

		if(MinecraftClient.getInstance().player.isUsingRiptide())return false;
		if(MinecraftClient.getInstance().player.isFallFlying())return false;
		if(MinecraftClient.getInstance().player.isSpectator())return false;
		if(!FirstPersonModelMod.enabled)return false;
		return true;
	}



}