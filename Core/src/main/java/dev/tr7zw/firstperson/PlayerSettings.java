package dev.tr7zw.firstperson;

import java.util.UUID;

import dev.tr7zw.firstperson.config.CosmeticSettings;

public interface PlayerSettings {
	
	public void setCosmeticSettings(CosmeticSettings settings);
	
	public CosmeticSettings getCosmeticSettings();
	
	public UUID getUUID();

}
