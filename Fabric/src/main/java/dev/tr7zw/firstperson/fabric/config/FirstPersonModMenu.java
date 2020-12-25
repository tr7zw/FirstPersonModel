package dev.tr7zw.firstperson.fabric.config;

import dev.tr7zw.velvet.api.Velvet;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class FirstPersonModMenu implements ModMenuApi{


	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
    	return parent -> {
    		return new ConfigBuilder().createConfigScreen(Velvet.velvet.getWrapper().wrapScreen(parent)).getHandler(Screen.class);
    	};
    }

	
    
}
  