package de.tr7zw.firstperson.mixinbase;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelCore;

public interface CameraBase {

	public default void isThirdPerson(boolean thirdPerson, CallbackInfoReturnable<Boolean> cir) {
		if (!thirdPerson)
			cir.setReturnValue(FirstPersonModelCore.instance.getWrapper().applyThirdPerson(false));
	}
	
}
