package de.tr7zw.firstperson;

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
}
