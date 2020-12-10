package de.tr7zw.firstperson.config;

import de.tr7zw.firstperson.features.LayerMode;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

public class SkinLayerSettings {

	public LayerMode headLayerMode = LayerMode.VANILLA2D;
	
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 8, max = 32)
	public int optimizedLayerDistance = 16;
	
	public LayerMode skinLayerMode = LayerMode.VANILLA2D;
	
}
