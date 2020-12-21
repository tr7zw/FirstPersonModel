package de.tr7zw.firstperson.features;

import de.tr7zw.firstperson.features.AbstractCosmetic.ModelCreator;

public interface FeatureRenderer {

	public ModelCreator getVanillaModelCreator(int u, int v);
	
	public Object getRenderLayerPlayerTextureCutout(Object player);
	
}
