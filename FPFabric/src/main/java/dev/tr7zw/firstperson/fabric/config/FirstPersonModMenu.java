package dev.tr7zw.firstperson.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.tr7zw.firstperson.FirstPersonModelCore;

public class FirstPersonModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return FirstPersonModelCore.instance.createConfigScreen(parent);
        };
    }  
    
}
