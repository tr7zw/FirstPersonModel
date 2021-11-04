package dev.tr7zw.firstperson.features.chest;

import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.Chest;
import dev.tr7zw.firstperson.features.FeatureRenderer;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class Female1Feature extends AbstractCosmetic{

	private ModelCreator model;
	
	@Override
	public void init(FeatureRenderer featureRenderer) {
		model = featureRenderer.getCustomModelCreator(0,0);
	    model.setTextureOffset(20, 20);
	    model.addCuboid(-3.0F, 1.0F, -3.0F, 6, 1, 1);
	    model.setTextureOffset(19, 21);
	    model.addCuboid(-4.0F, 2.0F, -3.0F, 8, 3, 1);
	    
	    model.setTextureOffset(20, 21);
	    model.addCuboid(-3.0F, 2.0F, -4.0F, 6, 1, 1);
	    model.setTextureOffset(19, 22);
	    model.addCuboid(-4.0F, 3.0F, -4.0F, 8, 1, 1);
	    model.setTextureOffset(20, 23);
	    model.addCuboid(-3.0F, 4.0F, -4.0F, 2, 1, 1);
	    model.setTextureOffset(24, 23);
	    model.addCuboid(1.0F, 4.0F, -4.0F, 2, 1, 1);
	}

	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Chest.FEMALE2);
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
