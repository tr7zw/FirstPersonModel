package dev.tr7zw.valvet.forge;

import dev.tr7zw.valvet.forge.config.ConfigBuilderImpl;
import dev.tr7zw.valvet.forge.registry.KeybindingsImpl;
import dev.tr7zw.valvet.forge.wrapper.WrapperImpl;
import dev.tr7zw.velvet.api.VelvetAPI;
import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.registry.Keybindings;
import dev.tr7zw.velvet.api.wrapper.Wrapper;

public class ValvetImpl implements VelvetAPI {

	private Wrapper wrapper = new WrapperImpl();
	private Keybindings keybindings = new KeybindingsImpl();
	
	@Override
	public Wrapper getWrapper() {
		return wrapper;
	}

	@Override
	public ConfigBuilder getNewConfigBuilder() {
		return new ConfigBuilderImpl();
	}

	@Override
	public Keybindings getKeybindings() {
		return keybindings;
	}

}
