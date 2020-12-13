package de.tr7zw.firstperson.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.NativeImage;

public class SolidPixelWrapper {

	private static SolidPixelModelPart wrapBox(PlayerEntityModel<AbstractClientPlayerEntity> model, int width,
			int height, int depth, int textureU, int textureV, boolean topPivot, float rotationOffset) {
		SolidPixelModelPart wrapper = new SolidPixelModelPart(model);
		float pixelSize = 1f;
		float staticXOffset = -width/2f;
		float staticYOffset = topPivot ? + rotationOffset : -height + 0.6f;//-7.4f;
		float staticZOffset = -depth/2f;
		// Front/back
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				// front
				wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 1 + v);
				wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset, pixelSize, pixelSize, pixelSize);
				// back
				wrapper.setTextureOffset(textureU + 2*depth + width - 2 + u, textureV + depth - 1 + v); // 54 + u, 7 + v
				wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset + depth - 1, pixelSize, pixelSize, pixelSize);
			}
		}
		// sides
		for (int u = 0; u < depth; u++) {
			for (int v = 0; v < height; v++) {
				// left
				wrapper.setTextureOffset(textureU - 3 + depth - u, textureV + depth - 1 + v); // 30 + 7 - u, 7 + v
				wrapper.addCustomCuboid(staticXOffset + 0, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);
				// right
				wrapper.setTextureOffset(textureU -2 + depth + width + u, textureV + depth - 1 + v); // 46 + u
				wrapper.addCustomCuboid(staticXOffset + width - 1f, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);

			}
		}
		// top/bottom
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < depth; v++) {
				// top
				wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 2 - v); // 38 + u
				wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset, staticZOffset + v, pixelSize, pixelSize, pixelSize);
				// bottom
				wrapper.setTextureOffset(textureU + depth + width - 2 + u, textureV + depth - 2 - v); // 46 + u
				wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset + height - 1f, staticZOffset + v, pixelSize, pixelSize, pixelSize);
			}
		}
		return wrapper;
	}
	
	public static SolidPixelModelPart wrapBoxOptimized(NativeImage natImage, PlayerEntityModel<AbstractClientPlayerEntity> model, int width,
			int height, int depth, int textureU, int textureV, boolean topPivot, float rotationOffset) {
		if(natImage == null)return wrapBox(model, width, height, depth, textureU, textureV, topPivot, rotationOffset);
		SolidPixelModelPart wrapper = new SolidPixelModelPart(model);
		float pixelSize = 1f;
		float staticXOffset = -width/2f;
		float staticYOffset = topPivot ? + rotationOffset : -height + 0.6f;//-7.4f;
		float staticZOffset = -depth/2f;
		// Front/back
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				// front
				if(natImage.getPixelOpacity(textureU + depth + u, textureV + depth + v) != 0) {
					wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 1 + v);
					wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset, pixelSize, pixelSize, pixelSize);
				}
				// back
				if(natImage.getPixelOpacity(textureU + 2*depth + width + u, textureV + depth + v) != 0) {
					wrapper.setTextureOffset(textureU + 2*depth + width - 2 + u, textureV + depth - 1 + v); // 54 + u, 7 + v
					wrapper.addCustomCuboid(staticXOffset + u, staticYOffset + v, staticZOffset + depth - 1, pixelSize, pixelSize, pixelSize);
				}
			}
		}
		// sides
		for (int u = 0; u < depth; u++) {
			for (int v = 0; v < height; v++) {
				// left
				if(natImage.getPixelOpacity(textureU - 1 + depth - u, textureV + depth + v) != 0) {
					wrapper.setTextureOffset(textureU - 3 + depth - u, textureV + depth - 1 + v); // 30 + 7 - u, 7 + v
					wrapper.addCustomCuboid(staticXOffset + 0, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);
				}
				// right
				if(natImage.getPixelOpacity(textureU + depth + width + u, textureV + depth + v) != 0) {
					wrapper.setTextureOffset(textureU -2 + depth + width + u, textureV + depth - 1 + v); // 46 + u
					wrapper.addCustomCuboid(staticXOffset + width - 1f, staticYOffset + v, staticZOffset + u, pixelSize, pixelSize, pixelSize);
				}

			}
		}
		// top/bottom
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < depth; v++) {
				// top
				if(natImage.getPixelOpacity(textureU + depth + u, textureV + depth - 1 - v) != 0) {
					wrapper.setTextureOffset(textureU + depth - 2 + u, textureV + depth - 2 - v); // 38 + u
					wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset, staticZOffset + v, pixelSize, pixelSize, pixelSize);
				}
				// bottom
				if(natImage.getPixelOpacity(textureU + depth + width - 2 + u, textureV + depth - v) != 0) {
					wrapper.setTextureOffset(textureU + depth + width - 2 + u, textureV + depth - 2 - v); // 46 + u
					wrapper.addCustomCuboid(staticXOffset + 0 + u, staticYOffset + height - 1f, staticZOffset + v, pixelSize, pixelSize, pixelSize);
				}
			}
		}
		return wrapper;
	}

	
}
