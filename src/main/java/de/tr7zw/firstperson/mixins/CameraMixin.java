package de.tr7zw.firstperson.mixins;

import de.tr7zw.firstperson.StaticMixinMethods;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private boolean thirdPerson;

    @Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
    private void CameraInject(CallbackInfoReturnable<Boolean> cir){
        if(!this.thirdPerson)cir.setReturnValue(StaticMixinMethods.isThirdPerson(false));
        //cir.setReturnValue(true);
    }
}
