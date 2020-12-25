package dev.tr7zw.velvet.fabric;

import dev.tr7zw.velvet.api.VelvetAPI;
import dev.tr7zw.velvet.api.config.ConfigBuilder;
import dev.tr7zw.velvet.api.wrapper.Wrapper;
import dev.tr7zw.velvet.fabric.config.ConfigBuilderImpl;
import dev.tr7zw.velvet.fabric.wrapper.WrapperImpl;

public class VelvetImpl implements VelvetAPI {

	private final Wrapper wrapper = new WrapperImpl();
	
	@Override
	public Wrapper getWrapper() {
		return wrapper;
	}

	@Override
	public ConfigBuilder getNewConfigBuilder() {
		return new ConfigBuilderImpl();
	}

}
