package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.access.PlayerAccess;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerMixin implements PlayerAccess {

    @Shadow
    @Getter
    private Inventory inventory;

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    public void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> ci) {
        if (FirstPersonModelCore.instance.isRenderingPlayer() && Minecraft.getInstance().isSameThread()) {
            if (slot == EquipmentSlot.HEAD) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
            if ((slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) && FirstPersonModelCore.instance
                    .getLogicHandler().hideArmsAndItems(this.inventory.getSelected(), this.inventory.offhand.get(0))) {
                ci.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }
    }

    @Inject(method = "doAutoAttackOnTouch", at = @At("HEAD"))//TODO SPIN ATTACK MODEL OFFSET FIX
    protected void doAutoAttackOnTouch(LivingEntity livingEntity, CallbackInfo ci) {
        FirstPersonModelCore.instance.getLogicHandler().setSpinTimer(0);
    }

}
