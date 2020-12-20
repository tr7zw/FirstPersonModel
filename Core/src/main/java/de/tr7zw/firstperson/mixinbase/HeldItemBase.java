package de.tr7zw.firstperson.mixinbase;

import de.tr7zw.firstperson.FirstPersonModelCore;

public interface HeldItemBase {

	public default boolean skip() {
		return !FirstPersonModelCore.enabled || FirstPersonModelCore.config.firstPerson.vanillaHands;
	}

}
