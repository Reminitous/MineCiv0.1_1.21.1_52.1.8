package net.reminitous.civilizationsmod.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemUtils {

    /**
     * Get the value of an item stack for war calculations.
     *
     * @param stack The item stack
     * @return The value of the item
     */
    public static int getItemValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        String itemPath = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();

        switch (itemPath) {
            case "diamond":
                return 50;
            case "gold_ingot":
                return 20;
            case "iron_ingot":
                return 10;
            case "emerald":
                return 40;
            case "netherite_ingot":
                return 100;
            case "coal":
                return 2;
            case "cobblestone":
            case "stone":
                return 1;
            default:
                return 5; // Default value for other items
        }
    }
}