package de.tr7zw.firstperson.features;

import java.util.Arrays;
import java.util.List;

import de.tr7zw.firstperson.features.chest.Female1Feature;
import de.tr7zw.firstperson.features.chest.Female2Feature;
import de.tr7zw.firstperson.features.hat.Deadmau5EarsFeature;
import de.tr7zw.firstperson.features.hat.PlungerFeature;

public class FeatureProvider {

	private static List<AbstractCosmetic> features = Arrays.asList(new Deadmau5EarsFeature(), new PlungerFeature(), new Female1Feature(), new Female2Feature());
	
	public static List<AbstractCosmetic> getFeatures() {
		return features;
	}
	
}
