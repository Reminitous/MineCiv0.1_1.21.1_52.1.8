package net.reminitous.civilizationsmod.registry;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

// Your ModBlockEntities code

public class ModBlockEntities {

    // Deferred register for block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CivilizationsMod.MOD_ID);

    // Register the Monument Tile Entity
    public static final RegistryObject<BlockEntityType<MonumentTileEntity>> MONUMENT_TILE_ENTITY =
            BLOCK_ENTITIES.register("monument_tile_entity",
                    () -> BlockEntityType.Builder.of(MonumentTileEntity::new, ModBlocks.MONUMENT_BLOCK.get()).build(null));

}
