package dev.tr7zw.valvet.forge.registry;

import dev.tr7zw.velvet.api.registry.Keybindings;
import dev.tr7zw.velvet.api.wrapper.WrappedKeybind;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeybindingsImpl implements Keybindings{

	@Override
	public void registerKeybinding(WrappedKeybind keybind) {
		ClientRegistry.registerKeyBinding(keybind.getHandler(KeyBinding.class));
	}

}
