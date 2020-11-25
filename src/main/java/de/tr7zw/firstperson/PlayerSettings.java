package de.tr7zw.firstperson;

import java.util.UUID;

import de.tr7zw.firstperson.features.Chest;
import de.tr7zw.firstperson.render.SolidPixelModelPart;
import net.minecraft.client.texture.NativeImage;

public interface PlayerSettings {

	public int getCustomHeight();
	
	public SolidPixelModelPart getHeadLayers();
	
	public void setupHeadLayers(SolidPixelModelPart box);
	
	public SolidPixelModelPart[] getSkinLayers();
	
	public void setupSkinLayers(SolidPixelModelPart[] box);
	
	public Chest getChest();
	
	public UUID getUUID();

	public void setCustomHeight(int customHeight);
	
	public void setChest(Chest chest);
	
}
