package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerMixin {

    @Shadow
    private Inventory inventory;
    
    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    public void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayer() && Minecraft.getInstance().isSameThread()) {
            if (slot == EquipmentSlot.HEAD) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
            if (FirstPersonModelCore.instance.getLogicHandler().showVanillaHands(this.inventory.getSelected(), this.inventory.offhand.get(0))) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }
    }

}
