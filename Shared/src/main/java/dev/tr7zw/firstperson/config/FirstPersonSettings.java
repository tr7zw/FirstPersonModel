package dev.tr7zw.firstperson.config;

public class FirstPersonSettings {

	public boolean enabledByDefault = true;
	public int xOffset = 0;
	public int sneakXOffset = 0;

	public int sitXOffset = 0;

	public boolean renderStuckFeatures = true;
	public boolean vanillaHands = false;
	public boolean doubleHands = false;
	public boolean hideCosmeticsButton = false;

	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
	public boolean forceActive = false;
	
}
