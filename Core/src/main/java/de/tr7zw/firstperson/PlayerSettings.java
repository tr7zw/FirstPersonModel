package de.tr7zw.firstperson;

import java.util.UUID;

import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;

public interface PlayerSettings {

	public int getCustomHeight();
	
	public Object getHeadLayers();
	
	public void setupHeadLayers(Object box);
	
	public Object[] getSkinLayers();
	
	public void setupSkinLayers(Object[] box);
	
	public Hat getHat();
	public void setHat(Hat hat);
	
	public Chest getChest();
	public void setChest(Chest chest);
	
	public Head getHead();
	public void setHead(Head head);
	
	public Back getBack();
	public void setBack(Back back);
	
	public Boots getBoots();
	public void setBoots(Boots boots);
	
	public UUID getUUID();

	public void setCustomHeight(int customHeight);
	
}
