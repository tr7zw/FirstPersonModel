package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.mixinbase.CameraBase;
import net.minecraft.client.renderer.ActiveRenderInfo;

// CameraMixin equivelent
@Mixin(ActiveRenderInfo.class)
public class ActiveRenderInfoMixin implements CameraBase{

	@Shadow
	private boolean thirdPerson;

	@Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
	private void CameraInject(CallbackInfoReturnable<Boolean> cir) {
		isThirdPerson(thirdPerson, cir);
	}

}
