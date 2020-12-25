package dev.tr7zw.velvet.api.wrapper;

import dev.tr7zw.velvet.api.config.WrappedConfigEntry;

public interface Wrapper {

	public WrappedScreen wrapScreen(Object screen);
	
	public WrappedText wrapText(Object text);
	
	public WrappedText getTranslateableText(String text);
	
	public WrappedConfigEntry getWrappedConfigEntry(Object clazz);
	
	public WrappedKeybind createKeyBind(String name, int key, String namespace);
	
}
