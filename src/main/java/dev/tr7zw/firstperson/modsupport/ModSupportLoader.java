package dev.tr7zw.firstperson.modsupport;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.util.ModLoaderUtil;

public class ModSupportLoader {

    public static void loadSupport() {
        // spotless:off
		//#if FABRIC
		try {
			if (ModLoaderUtil.isModLoaded("freecam")) {
				FirstPersonAPI.registerPlayerHandler(new FreecamSupport());
				FirstPersonModelCore.LOGGER.info("Freecam support loaded.");
			}
		} catch (Throwable ex) {
			FirstPersonModelCore.LOGGER.warn("Error during initialization of mod support.", ex);
		}
		//#endif
		//spotless:on
    }

}
