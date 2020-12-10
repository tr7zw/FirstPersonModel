package de.tr7zw.firstperson.config;

import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import de.tr7zw.firstperson.features.LayerMode;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "firstperson")
public class FirstPersonConfig implements ConfigData {
	
	public FirstPersonSettings firstPerson = new FirstPersonSettings();
	public PaperDollSettings paperDoll = new PaperDollSettings();

	public Hat hat = Hat.VANILLA;
	public Head head = Head.VANILLA;
	public Chest chest = Chest.VANILLA;
	public Boots boots = Boots.VANILLA;
	public Back back = Back.VANILLA;
	
	
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 70, max = 100)
	public int playerSize = 100;
	
	@ConfigEntry.Gui.Tooltip
	public boolean modifyCameraHeight = false;
	
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
			boots = config.boots.getId();
			head = config.head.getId();
			back = config.back.getId();
			hat = config.hat.getId();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + back;
			result = prime * result + boots;
			result = prime * result + chest;
			result = prime * result + hat;
			result = prime * result + head;
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
			if (back != other.back)
				return false;
			if (boots != other.boots)
				return false;
			if (chest != other.chest)
				return false;
			if (hat != other.hat)
				return false;
			if (head != other.head)
				return false;
			if (height != other.height)
				return false;
			return true;
		}
	}
	
}