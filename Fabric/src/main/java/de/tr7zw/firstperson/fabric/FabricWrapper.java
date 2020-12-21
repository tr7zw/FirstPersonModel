package de.tr7zw.firstperson.fabric;

import java.awt.Color;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.blaze3d.platform.GlStateManager;

import de.tr7zw.firstperson.FirstPersonModelCore;
import de.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Perspective;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FabricWrapper implements MinecraftWrapper {

	private final MinecraftClient client;
	private Vec3d offset; //Current offset used for rendering
	
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

	@Override
	public boolean applyThirdPerson(boolean thirdPerson) {
		if(client.player.isUsingRiptide())return false;
		if(client.player.isFallFlying())return false;
		if(client.player.isSpectator())return false;
		if(!FirstPersonModelCore.enabled || thirdPerson)return false;
		return true;
	}

	@Override
	public void refreshPlayerSettings() {
		client.options.onPlayerModelPartChange();
	}

	@Override
	public void isThirdPersonTrigger(Object matrices) {
		if (applyThirdPerson(client.options.getPerspective() != Perspective.FIRST_PERSON)){
			FirstPersonModelMod.hideHeadWithMatrixStack = (MatrixStack) matrices;
		}
	}

	@Override
	public void updatePositionOffset(Object player, Object defValue, Object matrices) {
		if(player == client.getCameraEntity() && client.player.isSleeping() || !FirstPersonModelMod.fixBodyShadow((MatrixStack) matrices)) {
			offset = (Vec3d) defValue;
			return;
		}
		double x,y,z = x = y = z = 0;
		AbstractClientPlayerEntity abstractClientPlayerEntity_1;
		double realYaw;
		if(player == client.player && client.options.getPerspective() == Perspective.FIRST_PERSON && FirstPersonModelMod.isRenderingPlayer) {
			abstractClientPlayerEntity_1 = (AbstractClientPlayerEntity) player;
			realYaw = MathHelper.lerpAngleDegrees(client.getTickDelta(), abstractClientPlayerEntity_1.prevYaw, abstractClientPlayerEntity_1.yaw);
			FirstPersonModelMod.isRenderingPlayer = false;
		}else {
			offset = (Vec3d) defValue;
			return;
		}
		if (!abstractClientPlayerEntity_1.isMainPlayer() || client.getCameraEntity() == abstractClientPlayerEntity_1) {
			float bodyOffset;
			if(client.player.isInSwimmingPose()) {
				abstractClientPlayerEntity_1.bodyYaw = abstractClientPlayerEntity_1.headYaw;
				if(abstractClientPlayerEntity_1.prevPitch > 0) {
					bodyOffset = FirstPersonModelMod.swimUpBodyOffset;
				}else {
					bodyOffset = FirstPersonModelMod.swimDownBodyOffset;
				}
			}else if(abstractClientPlayerEntity_1.isSneaking()){
				bodyOffset = FirstPersonModelMod.sneakBodyOffset + (FirstPersonModelMod.config.firstPerson.sneakXOffset / 100f);
			}else if(abstractClientPlayerEntity_1.hasVehicle()) {
				realYaw = MathHelper.lerpAngleDegrees(client.getTickDelta(), abstractClientPlayerEntity_1.prevBodyYaw, abstractClientPlayerEntity_1.bodyYaw);
				bodyOffset = FirstPersonModelMod.inVehicleBodyOffset + (FirstPersonModelMod.config.firstPerson.sitXOffset / 100f);
			}else{
				bodyOffset = 0.25f + (FirstPersonModelMod.config.firstPerson.xOffset / 100f);
			}
			x += bodyOffset * Math.sin(Math.toRadians(realYaw));
			z -= bodyOffset * Math.cos(Math.toRadians(realYaw));
			if(client.player.isInSwimmingPose()) {
				if(abstractClientPlayerEntity_1.prevPitch > 0  && abstractClientPlayerEntity_1.isSubmergedInWater()) {
					y += 0.6f * Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}else {
					y += 0.01f * -Math.sin(Math.toRadians(abstractClientPlayerEntity_1.prevPitch));
				}
			}

		}
		Vec3d vec = new Vec3d(x, y, z);
		abstractClientPlayerEntity_1 = null;
		FirstPersonModelMod.isRenderingPlayer = false;
		offset = vec;
	}

	@Override
	public Object getOffset() {
		return offset;
	}

	@Override
	public boolean hasCustomSkin(Object player) {
		return !DefaultSkinHelper.getTexture(((AbstractClientPlayerEntity)player).getUuid()).equals(((AbstractClientPlayerEntity)player).getSkinTexture());
	}

	@Override
	public Object getSkinTexture(Object player) {
		NativeImage skin = new NativeImage(Format.ABGR, 64, 64, true);
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		AbstractTexture abstractTexture = textureManager.getTexture(((AbstractClientPlayerEntity)player).getSkinTexture());
		GlStateManager.bindTexture(abstractTexture.getGlId());
		skin.loadFromTextureImage(0, false);
		return skin;
	}

	@Override
	public Object changeHue(Object ido, int width, int height, int hue) {
		Identifier id = (Identifier) ido;
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		Identifier newId = new Identifier(id.getNamespace(), id.getPath() + "_" + hue);
		if(textureManager.getTexture(newId) != null) {
			return newId;
		}
		AbstractTexture abstractTexture = textureManager.getTexture(id);
		if (abstractTexture == null) {
			return id;
		}
		NativeImage skin = new NativeImage(Format.ABGR, width, height, true);
		GlStateManager.bindTexture(abstractTexture.getGlId());
		skin.loadFromTextureImage(0, false);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (skin.getPixelOpacity(x, y) != 0) {
					int RGBA = skin.getPixelColor(x, y);
					int alpha = NativeImage.getAlpha(RGBA);
					int R = (RGBA >> 16) & 0xff;
					int G = (RGBA >> 8) & 0xff;
					int B = (RGBA) & 0xff;
					float HSV[] = new float[3];
					Color.RGBtoHSB(R, G, B, HSV);
					Color fColor = Color.getHSBColor(HSV[0] + (hue/360f), HSV[1], HSV[2]);
					skin.setPixelColor(x, y, NativeImage.getAbgrColor(alpha, fColor.getRed(), fColor.getGreen(), fColor.getBlue()));
				}
			}
		}
		textureManager.registerTexture(newId, new NativeImageBackedTexture(skin));
		return newId;
	}

}
