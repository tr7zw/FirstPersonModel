package dev.tr7zw.firstperson.modsupport;

import dev.tr7zw.firstperson.api.*;
import net.xolt.freecam.*;

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
