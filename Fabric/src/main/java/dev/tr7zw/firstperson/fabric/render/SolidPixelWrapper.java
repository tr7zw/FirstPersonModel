package dev.tr7zw.firstperson.fabric.render;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.math.Direction;

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
	
	public static CustomizableModelPart wrapBoxOptimized(NativeImage natImage, PlayerEntityModel<AbstractClientPlayerEntity> model, int width,
			int height, int depth, int textureU, int textureV, boolean topPivot, float rotationOffset) {
		//if(natImage == null)return wrapBox(model, width, height, depth, textureU, textureV, topPivot, rotationOffset);
		CustomizableModelPart wrapper = new CustomizableModelPart(model);
		float pixelSize = 1f;
		float staticXOffset = -width/2f;
		float staticYOffset = topPivot ? + rotationOffset : -height + 0.6f;//-7.4f;
		float staticZOffset = -depth/2f;
		// Front/back
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				// front
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU + depth + u, textureV + depth + v,
						staticXOffset + u, staticYOffset + v, staticZOffset, Direction.SOUTH);
				// back
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU + 2*depth + width + u, textureV + depth + v,
						staticXOffset + width - 1 - u, staticYOffset + v, staticZOffset + depth - 1, Direction.NORTH);
			}
		}
		
		// sides
		for (int u = 0; u < depth; u++) {
			for (int v = 0; v < height; v++) {
				// left
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU - 1 + depth - u, textureV + depth + v,
						staticXOffset, staticYOffset + v, staticZOffset + u, Direction.EAST);
				// right
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU + depth + width + u, textureV + depth + v,
						staticXOffset + width - 1f, staticYOffset + v, staticZOffset + u, Direction.WEST);

			}
		}
		// top/bottom
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < depth; v++) {
				// top
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU + depth + u, textureV + depth - 1 - v,
						staticXOffset + u, staticYOffset, staticZOffset + v, Direction.UP); // Sides are flipped cause ?!?
				// bottom
				addPixel(natImage, wrapper, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1,
						textureU + depth + width + u, textureV + depth - 1 - v,
						staticXOffset + u, staticYOffset + height - 1f, staticZOffset + v, Direction.DOWN); // Sides are flipped cause ?!?
			}
		}

		return wrapper;
	}
	
	private static int[][] offsets = new int[][] {{0,1}, {0,-1},{1,0},{-1,0}};
	private static Direction[] hiddenDirN = new Direction[] {Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN};
	private static Direction[] hiddenDirS = new Direction[] {Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN};
	private static Direction[] hiddenDirW = new Direction[] {Direction.SOUTH, Direction.NORTH, Direction.UP, Direction.DOWN};
	private static Direction[] hiddenDirE = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN};
	private static Direction[] hiddenDirUD = new Direction[] {Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH};
	
	private static void addPixel(NativeImage natImage,
			CustomizableModelPart wrapper, float pixelSize, boolean onBorder, int u, int v, float x, float y, float z, Direction dir) {
		if(natImage.getPixelOpacity(u, v) != 0) {
			Set<Direction> hide = new HashSet<>();
			if(!onBorder) {
				for(int i = 0; i < offsets.length; i++) {
					int tU = u+ offsets[i][1];
					int tV = v+ offsets[i][0];
					if(tU >= 0 && tU < 64 && tV >= 0 && tV < 64 && natImage.getPixelOpacity(tU, tV) != 0) {
						if(dir == Direction.NORTH)hide.add(hiddenDirN[i]);
						if(dir == Direction.SOUTH)hide.add(hiddenDirS[i]);
						if(dir == Direction.EAST)hide.add(hiddenDirE[i]);
						if(dir == Direction.WEST)hide.add(hiddenDirW[i]);
						if(dir == Direction.UP || dir == Direction.DOWN) {
							hide.add(hiddenDirUD[i]);
						}
					}
				}
				hide.add(dir);
			}
			wrapper.setTextureOffset(u-2, v-1);
			wrapper.addCustomCuboid(x, y, z, pixelSize, pixelSize, pixelSize, hide.toArray(new Direction[hide.size()]));
		}
	}

	
}
