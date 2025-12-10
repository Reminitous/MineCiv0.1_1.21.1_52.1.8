package net.reminitous.civilizationsmod.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.block.MonumentBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CivilizationsMod.MODID);

    public static final RegistryObject<Block> MONUMENT = BLOCKS.register("monument",
            () -> new MonumentBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.5f).sound(SoundType.STONE))
    );
}
