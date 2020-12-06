package de.tr7zw.firstperson;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public class FirstPersonModMenu implements ModMenuApi{

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    }
}
  