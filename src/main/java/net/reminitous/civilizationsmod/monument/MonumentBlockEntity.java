package net.reminitous.civilizationsmod.monument;

import net.reminitous.civilizationsmod.registry.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public class MonumentBlockEntity extends BlockEntity {

    private String civilizationId = "";

    public MonumentBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MONUMENT_BE.get(), pos, state);
    }

    public void setCivilizationId(String id) { this.civilizationId = id; }
    public String getCivilizationId() { return civilizationId; }
}
