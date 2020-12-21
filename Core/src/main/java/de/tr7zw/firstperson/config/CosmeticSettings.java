package de.tr7zw.firstperson.config;

import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;

public class CosmeticSettings {

	public Hat hat = Hat.VANILLA;
	public Head head = Head.VANILLA;
	public Chest chest = Chest.VANILLA;
	public Boots boots = Boots.VANILLA;
	public Back back = Back.VANILLA;
	
	public int playerSize = 100;
	public boolean modifyCameraHeight = false;
	public int backHue = 0;
	
	
	public SyncSnapshot createSnapshot() {
		return new SyncSnapshot(this);
	}
	
	public static class SyncSnapshot{
		public int height;
		public int backHue;
		public int chest;
		public int boots;
		public int head;
		public int back;
		public int hat;
		public SyncSnapshot(CosmeticSettings config) {
			height = config.playerSize;
			chest = config.chest.getId();
			boots = config.boots.getId();
			head = config.head.getId();
			back = config.back.getId();
			hat = config.hat.getId();
			backHue = config.backHue;
		}
		
		public CosmeticSettings toSettings() {
			CosmeticSettings settings = new CosmeticSettings();
			settings.playerSize = height;
			settings.chest = Chest.getChest(chest);
			settings.boots = Boots.getBoots(boots);
			settings.head = Head.getHead(head);
			settings.back = Back.getBack(back);
			settings.hat = Hat.getHat(hat);
			settings.backHue = backHue;

			return settings;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + back;
			result = prime * result + backHue;
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
			if (backHue != other.backHue)
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
