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

}