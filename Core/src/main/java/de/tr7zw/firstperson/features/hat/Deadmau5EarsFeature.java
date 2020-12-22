package de.tr7zw.firstperson.features.hat;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.features.AbstractCosmetic;
import de.tr7zw.firstperson.features.FeatureRenderer;
import de.tr7zw.firstperson.features.Hat;
import de.tr7zw.firstperson.util.SettingsUtil;

public class Deadmau5EarsFeature extends AbstractCosmetic {

	@Override
	public void init() {
	}
	
	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Hat.DEADMAU5)
				&& !FirstPersonModelCore.instance.isFixActive(player, matrixStack);
	}

	@Override
	public ModelCreator getModel(FeatureRenderer featureRenderer) {
		ModelCreator model = featureRenderer.getVanillaModelCreator(25, 1);
		model.addCuboid(3.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1); // left
		model.setTextureOffset(25, 1);
		model.addCuboid(-9.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1, true); // right
		return model;
	}

	@Override
	public Object getRenderLayer(FeatureRenderer featureRenderer, Object player) {
		return featureRenderer.getRenderLayerEntityCutout(player);
	}

	@Override
	public AttatchedTo getAttachedTo() {
		return AttatchedTo.HEAD;
	}

	
}
