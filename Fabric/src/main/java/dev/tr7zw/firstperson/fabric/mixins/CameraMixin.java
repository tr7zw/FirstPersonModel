package dev.tr7zw.firstperson.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.mixinbase.CameraBase;
import net.minecraft.client.render.Camera;

/**
 * Redirects the isThirdPerson call to "trick" Minecraft to render the Player
 * during firstperson
 *
 */
@Mixin(Camera.class)
public class CameraMixin implements CameraBase{
	
	@Shadow
	private boolean thirdPerson;

	@Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
	private void CameraInject(CallbackInfoReturnable<Boolean> cir) {
		isThirdPerson(thirdPerson, cir);
	}

}
