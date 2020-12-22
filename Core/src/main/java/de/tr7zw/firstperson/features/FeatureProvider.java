package de.tr7zw.firstperson.features;

import java.util.ArrayList;
import java.util.List;

import de.tr7zw.firstperson.features.hat.Deadmau5EarsFeature;

public class FeatureProvider {

	public static List<AbstractCosmetic> getFeatures() {
		List<AbstractCosmetic> cosmetics = new ArrayList<>();
		cosmetics.add(new Deadmau5EarsFeature());
		
		return cosmetics;
	}
	
}
