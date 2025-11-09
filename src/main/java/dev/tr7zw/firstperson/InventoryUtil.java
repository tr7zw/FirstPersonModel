package dev.tr7zw.firstperson;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

import java.util.*;

public class InventoryUtil {

    public static Inventory getInventory(Player player) {
        //? if >= 1.17.0 {

        return player.getInventory();
        //? } else {
        /*
         return player.inventory;
        *///? }
    }

    public static ItemStack getSelected(Inventory inventory) {
        //? if >= 1.21.5 {
        
        return inventory.getSelectedItem();
        //? } else {
/*
        return inventory.getSelected();
        *///? }
    }

    public static ItemStack getOffhand(Inventory inventory) {
        //? if >= 1.21.5 {
        
        return inventory.getItem(Inventory.SLOT_OFFHAND);
        //? } else {
/*
        return inventory.offhand.get(0);
        *///? }
    }

    public static int getSelectedId(Inventory inventory) {
        //? if >= 1.21.5 {
        
        return inventory.getSelectedSlot();
        //? } else {
/*
        return inventory.selected;
        *///? }
    }

    public static List<ItemStack> getNonEquipmentItems(Inventory inventory) {
        //? if >= 1.21.5 {
        
        return inventory.getNonEquipmentItems();
        //? } else {
/*
        return inventory.items;
        *///? }
    }

}
