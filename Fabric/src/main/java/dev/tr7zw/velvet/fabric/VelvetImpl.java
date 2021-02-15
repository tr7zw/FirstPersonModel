package dev.tr7zw.velvet.fabric;

import dev.tr7zw.velvet.api.VelvetAPI;
import dev.tr7zw.velvet.api.registry.Keybindings;
import dev.tr7zw.velvet.api.wrapper.Wrapper;
import dev.tr7zw.velvet.fabric.registry.KeybindingsImpl;
import dev.tr7zw.velvet.fabric.wrapper.WrapperImpl;

public class VelvetImpl implements VelvetAPI {

	private final Wrapper wrapper = new WrapperImpl();
	private final Keybindings keybindings = new KeybindingsImpl();
	
	@Override
	public Wrapper getWrapper() {
		return wrapper;
	}

	@Override
	public Keybindings getKeybindings() {
		return keybindings;
	}

}
