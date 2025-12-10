package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.EnumSet;

public class EngineerGoal extends Goal {

    private final Monster engineer;

    public EngineerGoal(Monster engineer) {
        this.engineer = engineer;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(engineer.getX(), engineer.getY(), engineer.getZ(),
                CivilizationManager.getCivilization(engineer));
    }

    @Override
    public void tick() {
        BlockPos pos = engineer.blockPosition();
        // Engineer can automate smelting or deposit items in civilization chests
        // Implementation can interact with a CivilizationStorage system
    }
}
