package dev.tr7zw.firstperson.versionless.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstPersonSettings {

    public int configVersion = 2;
    public boolean enabledByDefault = true;
    public int xOffset = 0;
    public int sneakXOffset = 0;

    public int sitXOffset = 0;

    public boolean renderStuckFeatures = true;
    public boolean vanillaHands = false;
    public boolean doubleHands = false;

    public Set<String> autoVanillaHands = new HashSet<>(Arrays.asList("antiqueatlas:antique_atlas",
            "twilightforest:filled_magic_map", "twilightforest:filled_maze_map", "twilightforest:filled_ore_map",
            "create:potato_cannon", "create:extendo_grip", "create:handheld_worldshaper", "map_atlases:atlas"));

    public Set<String> autoToggleModItems = new HashSet<>(Arrays.asList("exposure:camera"));

}
