package dev.tr7zw.firstperson.modsupport;

import dev.tr7zw.firstperson.api.FirstPersonAPI;
import dev.tr7zw.firstperson.versionless.FirstPersonBase;
import dev.tr7zw.util.ModLoaderUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModSupportLoader {

    public static void loadSupport() {
        try {
            if (ModLoaderUtil.isModLoaded("freecam")) {
                FirstPersonAPI.registerPlayerHandler(new FreecamSupport());
                FirstPersonBase.LOGGER.info("Freecam support loaded.");
            }
        } catch (Throwable ex) {
            FirstPersonBase.LOGGER.warn("Error during initialization of mod support.", ex);
        }
    }

}
