package de.tr7zw.firstperson.fabric.util;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

public class TextureUtil {

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

	public static Identifier changeHue(Identifier id, int width, int height, int hue) {
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
