package dev.tr7zw.firstperson.forge;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.blaze3d.matrix.MatrixStack;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.SystemToast.Type;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.network.play.client.CClientSettingsPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;

public class ForgeWrapper implements MinecraftWrapper{

	private final Minecraft client;
	private Vector3d offset; //Current offset used for rendering
	
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
	
	@Override
	public void isThirdPersonTrigger(Object matrices) {
		if (applyThirdPerson(client.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON)){
			FirstPersonModelMod.hideHeadWithMatrixStack = (MatrixStack) matrices;
		}
	}

	@Override
	public void updatePositionOffset(Object player, Object defValue, Object matrices) {
		if(player == client.getRenderViewEntity() && client.player.isSleeping() || !FirstPersonModelMod.fixBodyShadow((MatrixStack) matrices)) {
			offset = (Vector3d) defValue;
			return;
		}
		double x,y,z = x = y = z = 0;
		ClientPlayerEntity abstractClientPlayerEntity_1;
		double realYaw;
		if(player == client.player && client.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON && FirstPersonModelMod.isRenderingPlayer) {
			abstractClientPlayerEntity_1 =  (ClientPlayerEntity) player;
			realYaw = MathHelper.interpolateAngle(client.getRenderPartialTicks(), abstractClientPlayerEntity_1.prevRotationYaw, abstractClientPlayerEntity_1.rotationYaw);
			FirstPersonModelMod.isRenderingPlayer = false;
		}else {
			offset = (Vector3d) defValue;
			return;
		}
		if (!abstractClientPlayerEntity_1.isUser() || client.getRenderViewEntity() == abstractClientPlayerEntity_1) {
			float bodyOffset;
			if(client.player.isActualySwimming() ) {
				abstractClientPlayerEntity_1.renderYawOffset = abstractClientPlayerEntity_1.rotationYawHead;
				if(abstractClientPlayerEntity_1.prevRotationPitch > 0) {
					bodyOffset = FirstPersonModelMod.swimUpBodyOffset;
				}else {
					bodyOffset = FirstPersonModelMod.swimDownBodyOffset;
				}
			}else if(abstractClientPlayerEntity_1.isSneaking()){
				bodyOffset = FirstPersonModelMod.sneakBodyOffset + (FirstPersonModelMod.config.firstPerson.sneakXOffset / 100f);
			}else if(abstractClientPlayerEntity_1.getRidingEntity() != null) {
				realYaw = MathHelper.interpolateAngle(client.getRenderPartialTicks(), abstractClientPlayerEntity_1.prevRotationYaw, abstractClientPlayerEntity_1.rotationYaw);
				bodyOffset = FirstPersonModelMod.inVehicleBodyOffset + (FirstPersonModelMod.config.firstPerson.sitXOffset / 100f);
			}else{
				bodyOffset = 0.25f + (FirstPersonModelMod.config.firstPerson.xOffset / 100f);
			}
			x += bodyOffset * Math.sin(Math.toRadians(realYaw));
			z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
			if(client.player.isActualySwimming()) {
				if(abstractClientPlayerEntity_1.prevRotationPitch > 0  && abstractClientPlayerEntity_1.isInWater()) {
					y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevRotationPitch));
				}else {
					y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevRotationPitch));
				}
			}

		}
		Vector3d vec = new Vector3d(x, y, z);
		abstractClientPlayerEntity_1 = null;
		FirstPersonModelMod.isRenderingPlayer = false;
		offset = vec;
	}

	@Override
	public Object getOffset() {
		return offset;
	}

}
