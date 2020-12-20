package de.tr7zw.firstperson.mixinbase;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelCore;

public interface HeadFeatureRendererBase {

	public default void process(Object livingEntity, Object matrixStack, CallbackInfo info) {
		if (FirstPersonModelCore.instance.isFixActive(livingEntity, matrixStack)) {
			info.cancel();
		}
	}
	
}
