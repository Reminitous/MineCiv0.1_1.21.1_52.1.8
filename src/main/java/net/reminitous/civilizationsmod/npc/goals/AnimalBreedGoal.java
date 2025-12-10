package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.List;
import java.util.EnumSet;

public class AnimalBreedGoal extends Goal {

    private final Villager shepherd;

    public AnimalBreedGoal(Villager shepherd) {
        this.shepherd = shepherd;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(shepherd.getX(), shepherd.getY(), shepherd.getZ(),
                CivilizationManager.getCivilization(shepherd));
    }

    @Override
    public void tick() {
        BlockPos pos = shepherd.blockPosition();
        List<Animal> animals = shepherd.level.getEntitiesOfClass(Animal.class,
                TerritoryManager.getBoundingBoxForCivilization(CivilizationManager.getCivilization(shepherd)));

        for (Animal a : animals) {
            if (a.canFallInLove()) {
                a.setInLove(null); // shepherd feeds items from civilization storage in a full implementation
            }
        }
    }
}
