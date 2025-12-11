package net.reminitous.civilizationsmod.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.item.MonumentItem;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CivilizationsMod.MODID);

    // Monument item - crafted to start a civilization
    public static final RegistryObject<Item> MONUMENT_ITEM =
            ITEMS.register("monument",
                    () -> new MonumentItem(new Item.Properties()));

    // Monument block item (for placing the block)
    public static final RegistryObject<BlockItem> MONUMENT_BLOCK_ITEM =
            ITEMS.register("monument_block",
                    () -> new BlockItem(ModBlocks.MONUMENT.get(), new Item.Properties()));
}