package dev.tr7zw.firstperson.versionless.config;

import java.util.Arrays;
import java.util.HashSet;

public class ConfigUpgrader {

	public static void upgradeConfig(FirstPersonSettings conf) {
		if (conf.configVersion < 1) {
			// re-init the auto vanilla hands set
			conf.autoVanillaHands
					.addAll(new HashSet<>(Arrays.asList("antiqueatlas:antique_atlas", "twilightforest:filled_magic_map",
							"twilightforest:filled_maze_map", "twilightforest:filled_ore_map", "create:potato_cannon",
							"create:extendo_grip", "create:handheld_worldshaper", "map_atlases:atlas")));
			conf.configVersion = 1;
		}
	}

}
