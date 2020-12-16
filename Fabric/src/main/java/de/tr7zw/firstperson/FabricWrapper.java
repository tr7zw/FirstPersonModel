package de.tr7zw.firstperson;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.text.LiteralText;

public class FabricWrapper implements MinecraftWrapper {

	private final MinecraftClient client;
	
	public FabricWrapper(MinecraftClient instance) {
		this.client = instance;
	}
	
	@Override
	public String joinServerSession(String serverId) {
		try {
			client.getSessionService().joinServer(
					client.getSession().getProfile(),
					client.getSession().getAccessToken(), serverId);
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
		client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP, new LiteralText(message), submessage == null ? null : new LiteralText(submessage)));
	}

	@Override
	public void showToastFailure(String message, String submessage) {
		client.getToastManager().add(new SystemToast(SystemToast.Type.WORLD_ACCESS_FAILURE, new LiteralText(message), submessage == null ? null : new LiteralText(submessage)));
	}

	@Override
	public void sendNoLayerClientSettings() {
		GameOptions options = client.options;
		//this blinks the outer layer once, signaling a reload of this player
		if(this.client.player != null && this.client.player.networkHandler != null)
			this.client.player.networkHandler.sendPacket(new ClientSettingsC2SPacket(options.language, options.viewDistance,
					options.chatVisibility, options.chatColors, 0, options.mainArm));
	}

	@Override
	public Object getPlayer() {
		return client.player;
	}

}
