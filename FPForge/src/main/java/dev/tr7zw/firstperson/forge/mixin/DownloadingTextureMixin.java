package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.NativeImage;

//PlayerSkinTexture
@Mixin(DownloadingTexture.class)
public class DownloadingTextureMixin {

	@Inject(method = "processLegacySkin", at = @At("HEAD"), cancellable = true)
	private static NativeImage processLegacySkin(NativeImage image, CallbackInfoReturnable<NativeImage> info) {
		boolean bl = image.getHeight() == 32;
		if (bl) {
			@SuppressWarnings("resource")
			NativeImage nativeImage = new NativeImage(64, 64, true);
			nativeImage.copyImageData(image);
			image.close();
			image = nativeImage;
			nativeImage.fillAreaRGBA(0, 32, 64, 32, 0);
			nativeImage.copyAreaRGBA(4, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyAreaRGBA(8, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyAreaRGBA(0, 20, 24, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(4, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(8, 20, 8, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(12, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(44, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyAreaRGBA(48, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyAreaRGBA(40, 20, 0, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(44, 20, -8, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(48, 20, -16, 32, 4, 12, true, false);
			nativeImage.copyAreaRGBA(52, 20, -8, 32, 4, 12, true, false);
		}

		setAreaOpaque(image, 0, 16, 32, 8);
		setAreaOpaque(image, 8, 0, 16, 8);
		if (bl) {
			setAreaTransparent(image, 32, 0, 64, 32);
		}

		setAreaOpaque(image, 0, 16, 64, 32);
		setAreaOpaque(image, 16, 48, 48, 64);
		info.setReturnValue(image);
		return image;
	}
	
	   private static void setAreaTransparent(NativeImage image, int x, int y, int width, int height) {
		      for(int i = x; i < width; ++i) {
		         for(int j = y; j < height; ++j) {
		            int k = image.getPixelRGBA(i, j);
		            if ((k >> 24 & 255) < 128) {
		               return;
		            }
		         }
		      }

		      for(int l = x; l < width; ++l) {
		         for(int i1 = y; i1 < height; ++i1) {
		            image.setPixelRGBA(l, i1, image.getPixelRGBA(l, i1) & 16777215);
		         }
		      }

		   }

		   private static void setAreaOpaque(NativeImage image, int x, int y, int width, int height) {
		      for(int i = x; i < width; ++i) {
		         for(int j = y; j < height; ++j) {
		            image.setPixelRGBA(i, j, image.getPixelRGBA(i, j) | -16777216);
		         }
		      }

		   }
	
}
