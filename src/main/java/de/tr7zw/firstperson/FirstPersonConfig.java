package de.tr7zw.firstperson;

import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.LayerMode;
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

	public Chest chest = Chest.VANILLA;
	
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
	
	public SyncSnapshot createSnapshot() {
		return new SyncSnapshot(this);
	}
	
	public static class SyncSnapshot{
		public int height;
		public int chest;
		public int boots;
		public int head;
		public int back;
		public int hat;
		public SyncSnapshot(FirstPersonConfig config) {
			height = config.playerSize;
			chest = config.chest.getId();
			boots = 0;
			head = 0;
			back = 0;
			hat = 0;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + chest;
			result = prime * result + height;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SyncSnapshot other = (SyncSnapshot) obj;
			if (chest != other.chest)
				return false;
			if (height != other.height)
				return false;
			return true;
		}
	}
	
}