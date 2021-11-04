package dev.tr7zw.firstperson.features.hat;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureRenderer;
import dev.tr7zw.firstperson.features.Hat;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class Deadmau5EarsFeature extends AbstractCosmetic {

	private ModelCreator model;
	
	@Override
	public void init(FeatureRenderer featureRenderer) {
		model = featureRenderer.getVanillaModelCreator(25, 1);
		model.addCuboid(3.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1); // left
		model.setTextureOffset(25, 1);
		model.addCuboid(-9.0F, -12.0F, -1.0F, 6.0F, 6.0F, 0F, 1, true); // right
	}
	
	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Hat.DEADMAU5)
				&& !FirstPersonModelCore.isRenderingPlayer;
	}

	@Override
	public ModelCreator getModel() {
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
