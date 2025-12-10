package net.reminitous.civilizationsmod.utils;

import net.minecraft.world.item.ItemStack;

public class ItemUtils {
    public static int getItemValue(ItemStack stack) {
        switch (stack.getItem().getRegistryName().getPath()) {
            case "diamond": return 50;
            case "gold_ingot": return 20;
            case "iron_ingot": return 10;
            default: return 1;
        }
    }
}
