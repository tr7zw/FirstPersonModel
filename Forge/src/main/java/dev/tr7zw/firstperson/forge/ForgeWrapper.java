package dev.tr7zw.firstperson.forge;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.SystemToast.Type;
import net.minecraft.network.play.client.CClientSettingsPacket;
import net.minecraft.util.text.StringTextComponent;

public class ForgeWrapper implements MinecraftWrapper{

	private final Minecraft client;
	
	public ForgeWrapper(Minecraft instance) {
		this.client = instance;
	}

	@Override
	public String joinServerSession(String serverId) {
		try {
			client.getSessionService().joinServer(
					client.getSession().getProfile(),
					client.getSession().getToken(), serverId);
		} catch (AuthenticationUnavailableException var3) {
			return "Servers-Unavailable!";
		} catch (InvalidCredentialsException var4) {
			return "invalidSession";
		} catch (AuthenticationException var5) {
			return var5.getMessage();
		}
		return null; // Valid request
	}

	@Override
	public GameProfile getGameprofile() {
		return client.getSession().getProfile();
	}

	@Override
	public void showToastSuccess(String message, String submessage) {
		client.getToastGui().add(new SystemToast(Type.WORLD_BACKUP, new StringTextComponent(message), submessage == null ? null : new StringTextComponent(submessage)));
	}

	@Override
	public void showToastFailure(String message, String submessage) {
		client.getToastGui().add(new SystemToast(Type.WORLD_ACCESS_FAILURE, new StringTextComponent(message), submessage == null ? null : new StringTextComponent(submessage)));
	}

	@Override
	public void sendNoLayerClientSettings() {
		GameSettings options = client.gameSettings;
		//this blinks the outer layer once, signaling a reload of this player
		if(this.client.player != null && this.client.player.connection != null)
			this.client.player.connection.sendPacket(new CClientSettingsPacket(options.language, options.renderDistanceChunks,
					options.chatVisibility, options.chatColor, 0, options.mainHand));
	}

	@Override
	public Object getPlayer() {
		return client.player;
	}

	@Override
	public boolean applyThirdPerson(boolean thirdPerson) {
		Minecraft client = Minecraft.getInstance();
		if(client.player.isElytraFlying())return false;
		if(client.player.isSpinAttacking())return false;
		if(client.player.isSpectator())return false;
		if(!FirstPersonModelCore.enabled || thirdPerson)return false;
		return true;
	}

	@Override
	public void refreshPlayerSettings() {
		client.gameSettings.sendSettingsToServer();
	}

}
