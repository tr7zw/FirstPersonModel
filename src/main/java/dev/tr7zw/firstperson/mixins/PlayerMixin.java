package dev.tr7zw.firstperson.mixins;

import dev.tr7zw.firstperson.*;
import net.minecraft.client.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

//? if >= 1.21.5 {
@Mixin(LivingEntity.class)
//? } else {
/*
@Mixin(Player.class)
*///? }
public class PlayerMixin {

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    public void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayer() && Minecraft.getInstance().isSameThread()
                && (Object) this instanceof Player player) {
            if (slot == EquipmentSlot.HEAD) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
            if ((slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)
                    && FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(Minecraft.getInstance().player,
                            InventoryUtil.getSelected(InventoryUtil.getInventory(player)),
                            InventoryUtil.getOffhand(InventoryUtil.getInventory(player)))) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }
    }

}
