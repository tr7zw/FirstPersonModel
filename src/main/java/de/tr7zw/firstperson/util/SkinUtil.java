package de.tr7zw.firstperson.util;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.client.util.DefaultSkinHelper;

public class SkinUtil {

	public static boolean hasCustomSkin(AbstractClientPlayerEntity player) {
		return !DefaultSkinHelper.getTexture(player.getUuid()).equals(player.getSkinTexture());
	}
	
	public static NativeImage getSkinTexture(AbstractClientPlayerEntity player) {
		NativeImage skin = new NativeImage(Format.ABGR, 64, 64, true);
		TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		AbstractTexture abstractTexture = textureManager.getTexture(player.getSkinTexture());
		GlStateManager.bindTexture(abstractTexture.getGlId());
		skin.loadFromTextureImage(0, false);
		return skin;
	}
	
}
