package net.reminitous.civilizationsmod.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.npc.goals.AnimalBreedGoal;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

public class ShepherdNPC extends Villager {

    public ShepherdNPC(EntityType<? extends Villager> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AnimalBreedGoal(this));

        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return TerritoryManager.isInsideCivilization(getX(), getY(), getZ(), CivilizationManager.getCivilization(this.mob));
            }
        });
    }
}
