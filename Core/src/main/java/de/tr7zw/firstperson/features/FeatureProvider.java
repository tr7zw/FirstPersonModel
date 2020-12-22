package de.tr7zw.firstperson.features;

import java.util.Arrays;
import java.util.List;

import de.tr7zw.firstperson.features.hat.Deadmau5EarsFeature;
import de.tr7zw.firstperson.features.hat.PlungerFeature;

public class FeatureProvider {

	private static List<AbstractCosmetic> features = Arrays.asList(new Deadmau5EarsFeature(), new PlungerFeature());
	
	public static List<AbstractCosmetic> getFeatures() {
		return features;
	}
	
}
