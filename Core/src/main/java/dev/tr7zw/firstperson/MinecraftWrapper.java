package dev.tr7zw.firstperson;

import com.mojang.authlib.GameProfile;

public interface MinecraftWrapper {

	public String joinServerSession(String serverId);
	public GameProfile getGameprofile();
	public void showToastSuccess(String message, String submessage);
	public void showToastFailure(String message, String submessage);
	public void sendNoLayerClientSettings();
	public Object getPlayer();
	public boolean applyThirdPerson(boolean thirdPerson);
	public void refreshPlayerSettings();
	/**
	 * run it, if the renderer is rendering our scene, to set the head hidden.
	 */
 	public void isThirdPersonTrigger(Object matrices);
 	public void updatePositionOffset(Object player, Object defValue, Object matrices);
 	public Object getOffset();
	public boolean hasCustomSkin(Object player);
	public Object getSkinTexture(Object player);
	public Object changeHue(Object id, int hue);
	public Object getIdentifier(String namespace, String id);
	
}
