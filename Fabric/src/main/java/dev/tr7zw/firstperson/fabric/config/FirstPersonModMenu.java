package dev.tr7zw.firstperson.fabric.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

public class FirstPersonModMenu implements ModMenuApi{

	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
    	return parent -> {
    		return new ConfigBuilder().createConfigScreen(transliteration.getWrapper().wrapScreen(parent)).getHandler(Screen.class);
    	};
    }

}
  