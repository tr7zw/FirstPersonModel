package de.tr7zw.firstperson;

import java.util.UUID;

import de.tr7zw.firstperson.config.CosmeticSettings;

public interface PlayerSettings {

	public Object getHeadLayers();
	
	public void setupHeadLayers(Object box);
	
	public Object[] getSkinLayers();
	
	public void setupSkinLayers(Object[] box);
	
	public void setCosmeticSettings(CosmeticSettings settings);
	
	public CosmeticSettings getCosmeticSettings();
	
	public UUID getUUID();

}
