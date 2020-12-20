package de.tr7zw.firstperson.util;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import de.tr7zw.firstperson.PlayerSettings;
import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;

/**
 * Extracted into this class so in the future adding multi suport/moveing where 
 *
 */
public class SettingsUtil {

	private static MinecraftWrapper wrapper = FirstPersonModelCore.instance.getWrapper();
	
	public static boolean hasEnabled(Object player, Head head) {
		boolean self = player == wrapper.getPlayer();
		Head selected = Head.VANILLA;
		if(self) {
			selected = FirstPersonModelCore.config.cosmetic.head;
		}else {
			selected = ((PlayerSettings)player).getHead();
		}
		return selected == head;
	}
	
	public static boolean hasEnabled(Object player, Hat hat) {
		boolean self = player == wrapper.getPlayer();
		Hat selected = Hat.VANILLA;
		if(self) {
			selected = FirstPersonModelCore.config.cosmetic.hat;
		}else {
			selected = ((PlayerSettings)player).getHat();
		}
		return selected == hat;
	}
	
	public static boolean hasEnabled(Object player, Chest chest) {
		boolean self = player == wrapper.getPlayer();
		Chest selected = Chest.VANILLA;
		if(self) {
			selected = FirstPersonModelCore.config.cosmetic.chest;
		}else {
			selected = ((PlayerSettings)player).getChest();
		}
		return selected == chest;
	}
	
	public static boolean hasEnabled(Object player, Back back) {
		boolean self = player == wrapper.getPlayer();
		Back selected = Back.VANILLA;
		if(self) {
			selected = FirstPersonModelCore.config.cosmetic.back;
		}else {
			selected = ((PlayerSettings)player).getBack();
		}
		return selected == back;
	}
	
	public static boolean hasEnabled(Object player, Boots boots) {
		boolean self = player == wrapper.getPlayer();
		Boots selected = Boots.VANILLA;
		if(self) {
			selected = FirstPersonModelCore.config.cosmetic.boots;
		}else {
			selected = ((PlayerSettings)player).getBoots();
		}
		return selected == boots;
	}
	
	public static int getBackHue(Object player) {
		boolean self = player == wrapper.getPlayer();
		if(self) {
			return FirstPersonModelCore.config.cosmetic.backHue;
		}else {
			return ((PlayerSettings)player).getBackHue();
		}
	}
		
}
