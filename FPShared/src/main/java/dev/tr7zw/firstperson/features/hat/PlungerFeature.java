package dev.tr7zw.firstperson.features.hat;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.FeatureRenderer;
import dev.tr7zw.firstperson.features.Hat;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class PlungerFeature extends AbstractCosmetic {

	private Object texture;
	private ModelCreator model;
	
	@Override
	public void initTextures() {
		texture = FirstPersonModelCore.getWrapper().getIdentifier("firstperson", "textures/features/hat/plunger.png");
	}

	@Override
	public void init(FeatureRenderer featureRenderer) {
		model = featureRenderer.getVanillaModelCreator(32, 32, 0, 0);
		model.setPivot(0.0F, -8.0F, 0.0F);
		model.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -2.0F, 5.0F, 3.0F, 5.0F, 0.0F, false);
		model.setTextureOffset(0, 9).addCuboid(-1.0F, -16.0F, 0.0F, 1.0F, 13.0F, 1.0F, 0.0F, false);
	}

	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Hat.PLUNGER)
				&& !FirstPersonModelCore.isRenderingPlayer;
	}

	@Override
	public ModelCreator getModel() {
		return model;
	}

	@Override
	public Object getRenderLayer(FeatureRenderer featureRenderer, Object player) {
		return featureRenderer.getRenderLayerEntitySolid(texture);
	}

	@Override
	public AttatchedTo getAttachedTo() {
		return AttatchedTo.HEAD;
	}

	
}
