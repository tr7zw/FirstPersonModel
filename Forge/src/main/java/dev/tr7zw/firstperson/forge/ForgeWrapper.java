package dev.tr7zw.firstperson.forge;

import com.mojang.authlib.GameProfile;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.SystemToast.Type;
import net.minecraft.util.text.StringTextComponent;

public class ForgeWrapper implements MinecraftWrapper{

	private final Minecraft client;
	
	public ForgeWrapper(Minecraft instance) {
		this.client = instance;
	}

	@Override
	public String joinServerSession(String serverId) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
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

}
