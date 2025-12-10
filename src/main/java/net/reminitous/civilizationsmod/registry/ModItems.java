package net.reminitous.civilizationsmod.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.monument.ModBlocks;

/**
 * Registers all custom items for Civilizations Mod.
 */
public class ModItems {

    // Deferred register for items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CivilizationsMod.MOD_ID);

    /**
     * Monument Item
     * Crafted by players to place a Monument block and start a civilization.
     */
    // ModItems.java
    public static final RegistryObject<Item> MONUMENT_ITEM = ITEMS.register("monument",
            () -> new MonumentItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    // You can register other items (resources, tools, class-specific items) here
}
