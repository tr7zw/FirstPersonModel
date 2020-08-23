package de.tr7zw.firstperson;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "firstperson")
public class FirstPersonConfig implements ConfigData {
    
	//public boolean enabled = true;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int xOffset = 0;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int sneakXOffset = 0;

	//@ConfigEntry.Gui.Tooltip
	//public boolean improvedCompatibility = true;
	
	@ConfigEntry.Gui.Tooltip
	public boolean enabledByDefault = true;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dollEnabled = false;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int dollXOffset = 0;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int dollYOffset = 0;
	
	@ConfigEntry.BoundedDiscrete(min = -40, max = 40)
	@ConfigEntry.Gui.Tooltip
	public int dollSize = 0;
	
	@ConfigEntry.BoundedDiscrete(min = -80, max = 80)
	@ConfigEntry.Gui.Tooltip
	public int dollLookingSides = 20;
	
	@ConfigEntry.BoundedDiscrete(min = -80, max = 80)
	@ConfigEntry.Gui.Tooltip
	public int dollLookingUpDown = -20;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dollLockedHead = false;

	@ConfigEntry.Gui.Tooltip
	public boolean forceActive = false;
	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
}