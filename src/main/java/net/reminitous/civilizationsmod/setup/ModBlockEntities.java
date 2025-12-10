package net.reminitous.civilizationsmod.setup;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.blockentity.MonumentBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CivilizationsMod.MODID);

    public static final RegistryObject<BlockEntityType<MonumentBlockEntity>> MONUMENT =
            BLOCK_ENTITIES.register("monument_be",
                    () -> BlockEntityType.Builder.of(MonumentBlockEntity::new, ModBlocks.MONUMENT.get()).build(null)
            );
}
