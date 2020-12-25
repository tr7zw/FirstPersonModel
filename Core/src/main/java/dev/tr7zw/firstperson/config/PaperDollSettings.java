package dev.tr7zw.firstperson.config;

public class PaperDollSettings {

	public boolean dollEnabled = false;
	public int dollXOffset = 0;
	public int dollYOffset = 0;
	public int dollSize = 0;
	public int dollLookingSides = 20;
	public int dollLookingUpDown = -20;
	public DollHeadMode dollHeadMode = DollHeadMode.FREE;

	public enum DollHeadMode{
		FREE, STATIC, LOCKED
	}
	
	
}
