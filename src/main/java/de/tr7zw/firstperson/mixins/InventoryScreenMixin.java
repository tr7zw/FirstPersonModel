package de.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.tr7zw.firstperson.FirstPersonModelMod;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

	@Inject(at = @At("HEAD"), method = "drawEntity")
	private static void drawEntity(int int_1, int int_2, int int_3, float float_1, float float_2, LivingEntity livingEntity_1, CallbackInfo info) {
		FirstPersonModelMod.inInventory = true;
		FirstPersonModelMod.hideNextHeadArmor = false;
		FirstPersonModelMod.hideNextHeadItem = false;
	}

}
