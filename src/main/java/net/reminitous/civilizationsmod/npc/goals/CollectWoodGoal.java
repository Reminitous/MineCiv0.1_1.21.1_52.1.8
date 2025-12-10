package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.EnumSet;

public class CollectWoodGoal extends Goal {

    private final Monster lumberjack;

    public CollectWoodGoal(Monster lumberjack) {
        this.lumberjack = lumberjack;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(lumberjack.getX(), lumberjack.getY(), lumberjack.getZ(),
                CivilizationManager.getCivilization(lumberjack));
    }

    @Override
    public void tick() {
        BlockPos pos = lumberjack.blockPosition();

        if (lumberjack.level.getBlockState(pos).getBlock() == Blocks.OAK_LOG ||
                lumberjack.level.getBlockState(pos).getBlock() == Blocks.BIRCH_LOG) {
            lumberjack.level.destroyBlock(pos, true);
            // Optionally deposit wood in civilization storage
        }
    }
}
