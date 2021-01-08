package dev.tr7zw.firstperson.config;

public class FirstPersonSettings {

	public boolean enabledByDefault = true;
	public boolean lockBodyOnItems = false;
	public int xOffset = 0;
	public int sneakXOffset = 0;

	public int sitXOffset = 0;

	public boolean vanillaHands = false;
	public boolean playerHeadSkins = false;

	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
	public boolean forceActive = false;
	
}
