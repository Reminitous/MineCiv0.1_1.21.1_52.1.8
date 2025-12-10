package net.reminitous.civilizationsmod.monument;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;

/**
 * Registers all custom blocks for the Civilizations Mod.
 */
public class ModBlocks {

    // Deferred register for blocks
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CivilizationsMod.MOD_ID);

    /**
     * Monument Block
     * Players place this to create a civilization.
     */
    public static final RegistryObject<Block> MONUMENT_BLOCK =
            BLOCKS.register("monument_block",
                    () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                            .strength(4.0f, 6.0f)  // Hardness & blast resistance
                            .requiresCorrectToolForDrops()
                    ));

    // You can register other blocks here in the future

}
