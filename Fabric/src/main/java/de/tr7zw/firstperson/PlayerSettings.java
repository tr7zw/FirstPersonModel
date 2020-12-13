package de.tr7zw.firstperson;

import java.util.UUID;

import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.features.Boots;
import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.features.Head;
import de.tr7zw.firstperson.render.SolidPixelModelPart;

public interface PlayerSettings {

	public int getCustomHeight();
	
	public SolidPixelModelPart getHeadLayers();
	
	public void setupHeadLayers(SolidPixelModelPart box);
	
	public SolidPixelModelPart[] getSkinLayers();
	
	public void setupSkinLayers(SolidPixelModelPart[] box);
	
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
