package de.tr7zw.mod;

import net.fabricmc.api.ModInitializer;

public class TrMod implements ModInitializer {
	
	//Helper var
	public static boolean hideNextHeadArmor = false;
	public static boolean inInventory = false;
	
	@Override
	public void onInitialize() {
		System.out.println("Hello Fabric world!");
	}
}
