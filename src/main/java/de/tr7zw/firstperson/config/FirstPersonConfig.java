package de.tr7zw.firstperson.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "firstperson")
public class FirstPersonConfig implements ConfigData {
	
	public FirstPersonSettings firstPerson = new FirstPersonSettings();
	public PaperDollSettings paperDoll = new PaperDollSettings();
	public CosmeticSettings cosmetic = new CosmeticSettings();
	public SkinLayerSettings skinLayer = new SkinLayerSettings();
	
}