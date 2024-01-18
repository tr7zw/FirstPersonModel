package dev.tr7zw.firstperson.fabric.modsupport;

import dev.tr7zw.firstperson.api.ActivationHandler;
import net.xolt.freecam.Freecam;

public class FreecamSupport implements ActivationHandler {

    public FreecamSupport() {
        // making sure the constructor throws an exception, in case something is wrong.
        Freecam.isEnabled();
    }

    @Override
    public boolean preventFirstperson() {
        return Freecam.isEnabled();
    }

}
