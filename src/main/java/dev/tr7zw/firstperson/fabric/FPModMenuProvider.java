package dev.tr7zw.firstperson.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.tr7zw.firstperson.config.ConfigScreenProvider;

public class FPModMenuProvider implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return ConfigScreenProvider.createConfigScreen(parent);
        };
    }

}
