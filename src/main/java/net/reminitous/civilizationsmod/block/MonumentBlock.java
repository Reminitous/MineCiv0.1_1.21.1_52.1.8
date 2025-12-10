package net.reminitous.civilizationsmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MonumentBlock extends Block {
    public MonumentBlock(Properties props) { super(props); }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new net.reminitous.civilizationsmod.blockentity.MonumentBlockEntity(pos, state);
    }
}
