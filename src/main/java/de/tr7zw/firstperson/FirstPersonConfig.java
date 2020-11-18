package de.tr7zw.firstperson;

import de.tr7zw.firstperson.layer.LayerMode;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "firstperson")
public class FirstPersonConfig implements ConfigData {
	
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
	public boolean enabledByDefault = true;
	
	@ConfigEntry.Gui.Tooltip
	public boolean dollEnabled = false;
	
	@ConfigEntry.Gui.Tooltip
	public boolean vanillaHands = false;
	
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

	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
	@ConfigEntry.Gui.Tooltip
	public boolean forceActive = false;

	public boolean femaleModel = false;
	
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 1, max = 200)
	public int playerSize = 100;
	
	@ConfigEntry.Gui.Tooltip
	public LayerMode layerMode = LayerMode.VANILLA2D;
	
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 8, max = 32)
	public int optimizedLayerDistance = 16;
	
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 5, max = 50)
	public int layerLimiter = 16;
	
	@ConfigEntry.Gui.Tooltip
	public LayerMode skinLayerMode = LayerMode.VANILLA2D;
	
}