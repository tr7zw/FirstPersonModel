package de.tr7zw.firstperson.mixins;

import de.tr7zw.firstperson.StaticMixinMethods;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private boolean thirdPerson;
    @Shadow private Entity focusedEntity;
    @Shadow private float lastCameraY;
    @Shadow private float cameraY;

    @Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
    private void CameraInject(CallbackInfoReturnable<Boolean> cir){
        if(!this.thirdPerson)cir.setReturnValue(StaticMixinMethods.isThirdPerson(false));
        //cir.setReturnValue(true);
    }
    
   /* @Inject(method = "updateEyeHeight", at = @At("HEAD"), cancellable = true)
	public void updateEyeHeight() {
		if (this.focusedEntity != null) {
			this.lastCameraY = this.cameraY;
			this.cameraY += (this.focusedEntity.getStandingEyeHeight() - this.cameraY) * 0.5f;
		}
	}*/
    
}
