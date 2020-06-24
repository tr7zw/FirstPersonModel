package de.tr7zw.firstperson;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class FirstPersonModMenu implements ModMenuApi{

    /*
	@Override
	public String getModId() {
		return "firstperson";
	}

     */


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    }
}
  