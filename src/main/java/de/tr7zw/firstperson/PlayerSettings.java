package de.tr7zw.firstperson;

import java.util.UUID;

import de.tr7zw.firstperson.features.Chest;

public interface PlayerSettings {

	public int getCustomHeight();
	
	public Chest getChest();
	
	public UUID getUUID();

	public void setCustomHeight(int customHeight);
	
	public void setChest(Chest chest);
	
}
