package dev.tr7zw.firstperson.features;

import dev.tr7zw.firstperson.features.AbstractCosmetic.ModelCreator;

public interface FeatureRenderer {

	public ModelCreator getVanillaModelCreator(int u, int v);
	
	public ModelCreator getVanillaModelCreator(int textureWith, int textureHeight, int u, int v);
	
	public ModelCreator getCustomModelCreator(int u, int v);
	
	public Object getRenderLayerEntityCutout(Object texture);
	
	public Object getRenderLayerEntitySolid(Object texture);
	
	public Object getRenderLayerEntityTranslucentCull(Object texture);
	
}
