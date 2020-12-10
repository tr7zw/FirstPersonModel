package de.tr7zw.firstperson.config;

import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

public class FirstPersonSettings {

	@ConfigEntry.Gui.Tooltip
	public boolean enabledByDefault = true;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int xOffset = 0;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int sneakXOffset = 0;

	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int sitXOffset = 0;

	
	@ConfigEntry.Gui.Tooltip
	public boolean vanillaHands = false;

	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
	@ConfigEntry.Gui.Tooltip
	public boolean forceActive = false;
	
}
