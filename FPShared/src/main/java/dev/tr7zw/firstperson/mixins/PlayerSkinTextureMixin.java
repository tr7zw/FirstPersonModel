package dev.tr7zw.firstperson.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HttpTexture.class)
public abstract class PlayerSkinTextureMixin {

	@Inject(method = "processLegacySkin", at = @At("HEAD"), cancellable = true)
	private NativeImage remapTexture(NativeImage image, CallbackInfoReturnable<NativeImage> info) {
		boolean bl = image.getHeight() == 32;
		if (bl) {
			@SuppressWarnings("resource")
			NativeImage nativeImage = new NativeImage(64, 64, true);
			nativeImage.copyFrom(image);
			image.close();
			image = nativeImage;
			nativeImage.fillRect(0, 32, 64, 32, 0);
			nativeImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
			nativeImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
			nativeImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
			nativeImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
			nativeImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
			nativeImage.copyRect(52, 20, -8, 32, 4, 12, true, false);
		}

		stripAlpha(image, 0, 16, 32, 8);
		stripAlpha(image, 8, 0, 16, 8);
		if (bl) {
			stripColor(image, 32, 0, 64, 32);
		}

		stripAlpha(image, 0, 16, 64, 32);
		stripAlpha(image, 16, 48, 48, 64);
		info.setReturnValue(image);
		return image;
	}
	
	private static void stripColor(NativeImage image, int x, int y, int width, int height) {
		int l;
		int m;
		for (l = x; l < width; ++l) {
			for (m = y; m < height; ++m) {
				int k = image.getPixelRGBA(l, m);
				if ((k >> 24 & 255) < 128) {
					return;
				}
			}
		}

		for (l = x; l < width; ++l) {
			for (m = y; m < height; ++m) {
				image.setPixelRGBA(l, m, image.getPixelRGBA(l, m) & 16777215);
			}
		}

	}

	private static void stripAlpha(NativeImage image, int x, int y, int width, int height) {
		for (int i = x; i < width; ++i) {
			for (int j = y; j < height; ++j) {
				image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | -16777216);
			}
		}

	}
	
}
