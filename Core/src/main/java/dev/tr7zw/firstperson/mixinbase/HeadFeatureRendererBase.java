package dev.tr7zw.firstperson.mixinbase;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.firstperson.FirstPersonModelCore;

public interface HeadFeatureRendererBase {

	public default void process(CallbackInfo info) {
		if (FirstPersonModelCore.isRenderingPlayer) {
			info.cancel();
		}
	}
	
}
