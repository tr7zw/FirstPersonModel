package de.tr7zw.firstperson.config;

public class PaperDollSettings {

	public boolean dollEnabled = false;
	public int dollXOffset = 0;
	public int dollYOffset = 0;
	public int dollSize = 0;
	public int dollLookingSides = 20;
	public int dollLookingUpDown = -20;
	public boolean dollLockedHead = false;

	public PaperDollSettings copy() {
		PaperDollSettings copy = new PaperDollSettings();
		copy.dollEnabled = dollEnabled;
		copy.dollXOffset = dollXOffset;
		copy.dollYOffset = dollYOffset;
		copy.dollSize = dollSize;
		copy.dollLookingSides = dollLookingSides;
		copy.dollLookingUpDown = dollLookingUpDown;
		copy.dollLockedHead = dollLockedHead;
		return copy;
	}
	
}
