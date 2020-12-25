package dev.tr7zw.firstperson.util;

import dev.tr7zw.firstperson.PlayerSettings;
import dev.tr7zw.firstperson.features.Back;
import dev.tr7zw.firstperson.features.Boots;
import dev.tr7zw.firstperson.features.Chest;
import dev.tr7zw.firstperson.features.Hat;
import dev.tr7zw.firstperson.features.Head;

/**
 * Extracted into this class so in the future adding multi suport/moveing where
 *
 */
public class SettingsUtil {

	public static boolean hasEnabled(Object player, Head head) {
		return ((PlayerSettings) player).getCosmeticSettings().head == head;
	}

	public static boolean hasEnabled(Object player, Hat hat) {
		return ((PlayerSettings) player).getCosmeticSettings().hat == hat;
	}

	public static boolean hasEnabled(Object player, Chest chest) {
		return ((PlayerSettings) player).getCosmeticSettings().chest == chest;
	}

	public static boolean hasEnabled(Object player, Back back) {
		return ((PlayerSettings) player).getCosmeticSettings().back == back;
	}

	public static boolean hasEnabled(Object player, Boots boots) {
		return ((PlayerSettings) player).getCosmeticSettings().boots == boots;
	}

	public static int getBackHue(Object player) {
		return ((PlayerSettings) player).getCosmeticSettings().backHue;
	}

}
