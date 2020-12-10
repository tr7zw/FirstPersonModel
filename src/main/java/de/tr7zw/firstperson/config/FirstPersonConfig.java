package de.tr7zw.firstperson.config;

import de.tr7zw.firstperson.features.LayerMode;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "firstperson")
public class FirstPersonConfig implements ConfigData {
	
	public FirstPersonSettings firstPerson = new FirstPersonSettings();
	public PaperDollSettings paperDoll = new PaperDollSettings();
	public CosmeticSettings cosmetic = new CosmeticSettings();
	
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
			height = config.cosmetic.playerSize;
			chest = config.cosmetic.chest.getId();
			boots = config.cosmetic.boots.getId();
			head = config.cosmetic.head.getId();
			back = config.cosmetic.back.getId();
			hat = config.cosmetic.hat.getId();
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