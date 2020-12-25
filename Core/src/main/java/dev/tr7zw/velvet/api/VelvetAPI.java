package dev.tr7zw.velvet.api;

import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.wrapper.Wrapper;

public interface VelvetAPI {
	
	public Wrapper getWrapper();
	
	public ConfigBuilder getNewConfigBuilder();
	
}
