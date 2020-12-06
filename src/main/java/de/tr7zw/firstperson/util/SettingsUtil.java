package de.tr7zw.firstperson.util;

import de.tr7zw.firstperson.FirstPersonModelMod;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;

/**
 * Extracted into this class so in the future adding multi suport/moveing where 
 *
 */
public class SettingsUtil {

	private static MinecraftClient client = MinecraftClient.getInstance();
	
	public static boolean hasEnabled(AbstractClientPlayerEntity player, Head head) {
		boolean self = player == client.player;
		Head selected = Head.VANILLA;
		if(self) {
			selected = FirstPersonModelMod.config.head;
		}else {
			selected = ((PlayerSettings)player).getHead();
		}
		return selected == head;
	}
	
	public static boolean hasEnabled(AbstractClientPlayerEntity player, Hat hat) {
		boolean self = player == client.player;
		Hat selected = Hat.VANILLA;
		if(self) {
			selected = FirstPersonModelMod.config.hat;
		}else {
			selected = ((PlayerSettings)player).getHat();
		}
		return selected == hat;
	}
	
	public static boolean hasEnabled(AbstractClientPlayerEntity player, Chest chest) {
		boolean self = player == client.player;
		Chest selected = Chest.VANILLA;
		if(self) {
			selected = FirstPersonModelMod.config.chest;
		}else {
			selected = ((PlayerSettings)player).getChest();
		}
		return selected == chest;
	}
	
	public static boolean hasEnabled(AbstractClientPlayerEntity player, Back back) {
		boolean self = player == client.player;
		Back selected = Back.VANILLA;
		if(self) {
			selected = FirstPersonModelMod.config.back;
		}else {
			selected = ((PlayerSettings)player).getBack();
		}
		return selected == back;
	}
	
	public static boolean hasEnabled(AbstractClientPlayerEntity player, Boots boots) {
		boolean self = player == client.player;
		Boots selected = Boots.VANILLA;
		if(self) {
			selected = FirstPersonModelMod.config.boots;
		}else {
			selected = ((PlayerSettings)player).getBoots();
		}
		return selected == boots;
	}
	
}
