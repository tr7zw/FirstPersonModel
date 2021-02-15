package dev.tr7zw.firstperson.fabric.config;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import dev.tr7zw.firstperson.config.SharedConfigBuilder;
import dev.tr7zw.transliterationlib.api.config.WrappedConfigEntry;

public class ConfigBuilder extends SharedConfigBuilder{

	@Override
	public WrappedConfigEntry getPreviewEntry() {
		return transliteration.getWrapper().getWrappedConfigEntry(new PlayerPreviewConfigEntry());
	}
	
}
