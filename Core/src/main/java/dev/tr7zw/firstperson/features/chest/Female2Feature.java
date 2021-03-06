package dev.tr7zw.firstperson.features.chest;

import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.Chest;
import dev.tr7zw.firstperson.features.FeatureRenderer;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class Female2Feature extends AbstractCosmetic{

	private ModelCreator model;
	
	@Override
	public void init(FeatureRenderer featureRenderer) {
		model = featureRenderer.getCustomModelCreator(0,0);
		model.setTextureOffset(20, 20);
		model.addCuboid(-3.0F, 1.0F, -3.0F, 6f, 1f, 1f);
		model.setTextureOffset(19, 21);
		model.addCuboid(-4.0F, 2.0F, -3.0F, 8f, 3f, 1f);
	}

	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Chest.FEMALE1);
	}

	@Override
	public ModelCreator getModel() {
		return model;
	}

	@Override
	public Object getRenderLayer(FeatureRenderer featureRenderer, Object player) {
		return featureRenderer.getRenderLayerEntitySolid(player);
	}

	@Override
	public AttatchedTo getAttachedTo() {
		return AttatchedTo.BODY;
	}

}
