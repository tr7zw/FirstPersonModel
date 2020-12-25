package dev.tr7zw.firstperson.fabric.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class FirstPersonModMenu implements ModMenuApi{


	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return screen -> AutoConfig.getConfigScreen(FirstPersonConfig.class, screen).get();
    	return parent -> {
    		return new FabricConfigBuilder().getConfigScreen(parent);
    	};
    }

	
    
}
  