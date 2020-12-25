package dev.tr7zw.velvet.api.config;

import dev.tr7zw.velvet.api.wrapper.NMSWrapper;

public interface WrappedConfigEntry extends NMSWrapper{

	public <T extends Enum<?>> T getEnumValue(Class<T> targetEnum);
	public int getIntValue();
	
}
