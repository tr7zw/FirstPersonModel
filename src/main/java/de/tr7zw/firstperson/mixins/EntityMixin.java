package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(at = @At("HEAD"), method = "isInvisible", cancellable = true)
	   public boolean isInvisible(CallbackInfoReturnable<Boolean> info) {
			if(FirstPersonModelMod.hideArms) {
				info.setReturnValue(true);
				FirstPersonModelMod.hideArms = false;
				return true;
			}
			return false;
		}
	   
}
