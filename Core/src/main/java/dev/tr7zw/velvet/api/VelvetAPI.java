package dev.tr7zw.velvet.api;

import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.wrapper.Wrapper;

/**
 * Used to wrap NMS and other mods like ClothConfig for Modloader independed usage.
 * 
 * @author tr7zw
 *
 */
public interface VelvetAPI {
	
	public Wrapper getWrapper();
	
	public ConfigBuilder getNewConfigBuilder();
	
}
