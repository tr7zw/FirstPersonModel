//? if fabric {

package dev.tr7zw.firstperson.fabric;

import com.terraformersmc.modmenu.api.*;
import dev.tr7zw.firstperson.config.*;

public class FPModMenuProvider implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreenProvider::createConfigScreen;
    }

}
//? }
