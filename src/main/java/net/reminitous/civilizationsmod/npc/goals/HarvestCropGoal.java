package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.territory.TerritoryManager;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;

import java.util.EnumSet;

public class HarvestCropGoal extends Goal {

    private final Villager farmer;

    public HarvestCropGoal(Villager farmer) {
        this.farmer = farmer;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(farmer.getX(), farmer.getY(), farmer.getZ(),
                CivilizationManager.getCivilization(farmer));
    }

    @Override
    public void tick() {
        BlockPos pos = farmer.blockPosition();
        BlockState state = farmer.level().getBlockState(pos.below());

        if (state.getBlock() instanceof CropBlock crop) {
            if (crop.isMaxAge(state)) {
                // Harvest the crop
                crop.harvestBlock(farmer.level(), pos.below(), state, farmer);
                // Optionally replant (simulate NPC behavior)
                farmer.level().setBlock(pos.below(), crop.defaultBlockState(), 3);
            }
        }
    }
}
