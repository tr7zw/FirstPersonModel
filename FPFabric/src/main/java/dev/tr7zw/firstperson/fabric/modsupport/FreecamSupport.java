package dev.tr7zw.firstperson.fabric.modsupport;

import dev.tr7zw.firstperson.api.ActivationHandler;
import net.xolt.freecam.Freecam;

public class FreecamSupport implements ActivationHandler {

    @Override
    public boolean preventFirstperson() {
        return Freecam.isEnabled();
    }

}
