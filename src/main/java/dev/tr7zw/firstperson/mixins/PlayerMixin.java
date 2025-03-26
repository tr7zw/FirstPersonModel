package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

//#if MC >= 12105
import net.minecraft.world.entity.LivingEntity;
@Mixin(LivingEntity.class)
//#else
//$$ @Mixin(Player.class)
//#endif
public class PlayerMixin {

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    public void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayer() && Minecraft.getInstance().isSameThread() && (Object)this instanceof Player player) {
            if (slot == EquipmentSlot.HEAD) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
            if ((slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)
                    && FirstPersonModelCore.instance.getLogicHandler().hideArmsAndItems(Minecraft.getInstance().player,
                            InventoryUtil.getSelected(player.getInventory()), InventoryUtil.getOffhand(player.getInventory()))) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }
    }

}
