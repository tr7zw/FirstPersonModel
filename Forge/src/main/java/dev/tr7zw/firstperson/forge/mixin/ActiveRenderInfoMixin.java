package dev.tr7zw.firstperson.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;

// CameraMixin equivelent
@Mixin(ActiveRenderInfo.class)
public class ActiveRenderInfoMixin {

	@Shadow
	private boolean thirdPerson;

	@Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
	private void CameraInject(CallbackInfoReturnable<Boolean> cir) {
		if (!this.thirdPerson)
			cir.setReturnValue(isThirdPerson(false));
	}
	
	public boolean isThirdPerson(boolean thirdPerson) {
		Minecraft client = Minecraft.getInstance();
		if(client.player.isElytraFlying())return false;
		if(client.player.isSpinAttacking())return false;
		if(client.player.isSpectator())return false;
		if(!enabled || thirdPerson)return false;
		return true;
	}
	
	private boolean enabled = true;
	
}
