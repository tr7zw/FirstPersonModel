package dev.tr7zw.firstperson.forge.config;

import dev.tr7zw.firstperson.config.SharedConfigBuilder;
import dev.tr7zw.velvet.api.Velvet;
import dev.tr7zw.velvet.api.config.WrappedConfigEntry;

public class ConfigBuilder extends SharedConfigBuilder{

	@Override
	public WrappedConfigEntry getPreviewEntry() {
		return Velvet.velvet.getWrapper().getWrappedConfigEntry(new PlayerPreviewConfigEntry());
	}

}
