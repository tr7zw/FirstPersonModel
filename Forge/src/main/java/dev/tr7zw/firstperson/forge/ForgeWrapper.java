package dev.tr7zw.firstperson.forge;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.MinecraftWrapper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.SystemToast.Type;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.NativeImage.PixelFormat;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.network.play.client.CClientSettingsPacket;
import net.minecraft.util.ResourceLocation;
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
				if(abstractClientPlayerEntity_1.getRidingEntity() instanceof BoatEntity) {
					realYaw = MathHelper.interpolateAngle(client.getRenderPartialTicks(), abstractClientPlayerEntity_1.prevRotationYaw, abstractClientPlayerEntity_1.rotationYaw);
				} else if(abstractClientPlayerEntity_1.getRidingEntity() instanceof LivingEntity) {
					realYaw = MathHelper.interpolateAngle(client.getRenderPartialTicks(), ((LivingEntity)abstractClientPlayerEntity_1.getRidingEntity()).prevRotationYaw, ((LivingEntity)abstractClientPlayerEntity_1.getRidingEntity()).rotationYaw);
				} else {
					realYaw = MathHelper.interpolateAngle(client.getRenderPartialTicks(), abstractClientPlayerEntity_1.getRidingEntity().prevRotationYaw, ((LivingEntity)abstractClientPlayerEntity_1.getRidingEntity()).rotationYaw);
				}
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
	
	@Override
	public boolean hasCustomSkin(Object player) {
		return !DefaultPlayerSkin.getDefaultSkin(((ClientPlayerEntity)player).getUniqueID()).equals(((ClientPlayerEntity)player).getLocationSkin());
	}

	@Override
	public Object getSkinTexture(Object player) {
		NativeImage skin = new NativeImage(PixelFormat.RGBA, 64, 64, true);
		TextureManager textureManager = client.getTextureManager();
		Texture abstractTexture = textureManager.getTexture(((ClientPlayerEntity)player).getLocationSkin());
		GlStateManager.bindTexture(abstractTexture.getGlTextureId());
		skin.downloadFromTexture(0, false);
		return skin;
	}

	@Override
	public Object changeHue(Object ido, int hue) {
		ResourceLocation id = (ResourceLocation) ido;
		TextureManager textureManager = client.getTextureManager();
		ResourceLocation newId = new ResourceLocation(id.getNamespace(), id.getPath() + "_" + hue);
		if(textureManager.getTexture(newId) != null) {
			return newId;
		}
		Texture abstractTexture = textureManager.getTexture(id);
		if (abstractTexture == null) {
			return id;
		}
		GlStateManager.bindTexture(abstractTexture.getGlTextureId());
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		NativeImage skin = new NativeImage(PixelFormat.RGBA, width, height, true);
		skin.downloadFromTexture(0, false);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (skin.getPixelLuminanceOrAlpha(x, y) != 0) {
					int RGBA = skin.getPixelRGBA(x, y);
					int alpha = NativeImage.getAlpha(RGBA);
					int R = (RGBA >> 16) & 0xff;
					int G = (RGBA >> 8) & 0xff;
					int B = (RGBA) & 0xff;
					float HSV[] = new float[3];
					Color.RGBtoHSB(R, G, B, HSV);
					Color fColor = Color.getHSBColor(HSV[0] + (hue/360f), HSV[1], HSV[2]);
					skin.setPixelRGBA(x, y, NativeImage.getCombined(alpha, fColor.getRed(), fColor.getGreen(), fColor.getBlue()));
				}
			}
		}
		textureManager.loadTexture(newId, new DynamicTexture(skin));
		return newId;
	}

	@Override
	public Object getIdentifier(String namespace, String id) {
		return new ResourceLocation(namespace, id);
	}

}
