package dev.tr7zw.firstperson.features.back;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.features.AbstractCosmetic;
import dev.tr7zw.firstperson.features.Back;
import dev.tr7zw.firstperson.features.FeatureRenderer;
import dev.tr7zw.firstperson.util.SettingsUtil;

public class WingFeature extends AbstractCosmetic{

	private Object butterflyTexture;
	private Object fantasyTexture;
	private ModelCreator model;
	private ModelCreator cube_r1;
	private ModelCreator cube_r2;
	
	@Override
	public void initTextures() {
		butterflyTexture = FirstPersonModelCore.instance.getWrapper().getIdentifier("firstperson", "textures/features/back/butterfly.png");
		fantasyTexture = FirstPersonModelCore.instance.getWrapper().getIdentifier("firstperson", "textures/features/back/fantasywings.png");
	}

	@Override
	public void init(FeatureRenderer featureRenderer) {
		model = featureRenderer.getVanillaModelCreator(40, 40, 0, 0);
		model.setPivot(0.0F, 12.0F, 0.0F);

		cube_r1 = featureRenderer.getVanillaModelCreator(40, 40, 0, 0);
		cube_r1.setPivot(0.0F, 0.0F, 2.0F);
		model.addChild(cube_r1);
		cube_r1.setRotationAngle(0.0F, 2.3562F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-19.5F, -20.0F, 0.5F, 20.0F, 20.0F, 0.0F, 0.0F, false);

		cube_r2 = featureRenderer.getVanillaModelCreator(40, 40, 0, 0);
		cube_r2.setPivot(0.0F, 0.0F, 2.0F);
		model.addChild(cube_r2);
		cube_r2.setRotationAngle(0.0F, 0.7854F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addCuboid(-19.5F, -20.0F, -0.5F, 20.0F, 20.0F, 0.0F, 0.0F, false);
	}

	@Override
	public boolean shouldRender(Object player, Object matrixStack) {
		return SettingsUtil.hasEnabled(player, Back.BUTTERFLY) || SettingsUtil.hasEnabled(player, Back.FANTASYWINGS);
	}

	@Override
	public ModelCreator getModel() {
		return model;
	}

	@Override
	public Object getRenderLayer(FeatureRenderer featureRenderer, Object player) {
		if(SettingsUtil.hasEnabled(player, Back.BUTTERFLY)) {
			return featureRenderer.getRenderLayerEntityCutout(FirstPersonModelCore.instance.getWrapper().changeHue(butterflyTexture, 512, 512, SettingsUtil.getBackHue(player)));
		}else if(SettingsUtil.hasEnabled(player, Back.FANTASYWINGS)) {
			return featureRenderer.getRenderLayerEntityTranslucentCull(FirstPersonModelCore.instance.getWrapper().changeHue(fantasyTexture, 512, 512, SettingsUtil.getBackHue(player)));
		} else {
			throw new RuntimeException("Tried getting Texture for player without using it!");
		}
	}

	@Override
	public AttatchedTo getAttachedTo() {
		return AttatchedTo.BODY;
	}

	@Override
	public void updateModel(Object player) {
		float timestep = System.currentTimeMillis()%4000;
		timestep /= 4000F;
		timestep *= 2*Math.PI;
		timestep = (float) Math.sin(timestep);
		cube_r1.setRotationAngle(0.0F, 2.3562F + (timestep * 0.1F), 0.0F);
		cube_r2.setRotationAngle(0.0F, 0.7854F + (-timestep * 0.1F), 0.0F);
	}

}
