package net.reminitous.civilizationsmod.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.block.MonumentBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "civilizationsmod");

    public static final RegistryObject<MonumentBlock> MONUMENT =
            BLOCKS.register("monument",
                    () -> new MonumentBlock(BlockBehaviour.Properties.of().strength(3.0f)));
}
