package net.reminitous.civilizationsmod.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reminitous.civilizationsmod.registry.ModBlockEntities;

public class MonumentBlockEntity extends BlockEntity {

    public MonumentBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MONUMENT.get(), pos, state);
    }

    // Example: add your custom data or methods here
}
