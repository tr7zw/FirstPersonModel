package de.tr7zw.firstperson;

import net.fabricmc.api.ModInitializer;

public class FirstPersonModelMod implements ModInitializer {

	public static boolean renderingFirstPersonPlayer = false;
	
	@Override
	public void onInitialize() {
		System.out.println("Loaded FirstPerson Models");
	}
}
