package dev.tr7zw.firstperson.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstPersonSettings {

	public boolean enabledByDefault = true;
	public int xOffset = 0;
	public int sneakXOffset = 0;

	public int sitXOffset = 0;

	public boolean renderStuckFeatures = true;
	public boolean vanillaHands = false;
	public boolean doubleHands = false;

	//Make the fixes force active. can solve problems in different renderers, and causing bugs
	//on -> no OF compatibility, Hidden heads in Immersive portal mirrors...
	public boolean forceActive = false;
	
    public Set<String> autoVanillaHands = new HashSet<>(
            Arrays.asList("antiqueatlas:antique_atlas", "twilightforest:filled_magic_map", "twilightforest:filled_maze_map", "twilightforest:filled_ore_map"));
	
}
