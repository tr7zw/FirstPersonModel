package dev.tr7zw.firstperson.features;

import java.util.Arrays;
import java.util.List;

import dev.tr7zw.firstperson.features.back.WingFeature;
import dev.tr7zw.firstperson.features.chest.Female1Feature;
import dev.tr7zw.firstperson.features.chest.Female2Feature;
import dev.tr7zw.firstperson.features.hat.Deadmau5EarsFeature;
import dev.tr7zw.firstperson.features.hat.PlungerFeature;

public class FeatureProvider {

	private static List<AbstractCosmetic> features = Arrays.asList(new Deadmau5EarsFeature(), new PlungerFeature(), new Female1Feature(), new Female2Feature(), new WingFeature());
	
	public static List<AbstractCosmetic> getFeatures() {
		return features;
	}
	
}
