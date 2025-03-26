package dev.tr7zw.firstperson;

import java.util.List;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil {

    public static ItemStack getSelected(Inventory inventory) {
        //#if MC >= 12105
        return inventory.getSelectedItem();
        //#else
        //$$ return inventory.getSelected();
        //#endif
    }
    
    public static ItemStack getOffhand(Inventory inventory) {
        //#if MC >= 12105
        return inventory.getItem(Inventory.SLOT_OFFHAND);
        //#else
        //$$ return inventory.offhand.get(0);
        //#endif
    }

    public static int getSelectedId(Inventory inventory) {
        //#if MC >= 12105
        return inventory.getSelectedSlot();
        //#else
        //$$ return inventory.selected;
        //#endif
    }

    public static List<ItemStack> getNonEquipmentItems(Inventory inventory) {
        //#if MC >= 12105
        return inventory.getNonEquipmentItems();
        //#else
        //$$ return inventory.items;
        //#endif
    }

}
