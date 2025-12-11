package net.reminitous.civilizationsmod.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.npc.goals.HarvestCropGoal;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

public class FarmerNPC extends Villager {

    public FarmerNPC(EntityType<? extends Villager> type, Level level) {
        super(type, level); // ✅ Pass type, not null
    }

    @Override
    protected void registerGoals() {
        // Harvest crops only within the territory
        this.goalSelector.addGoal(1, new HarvestCropGoal(this));

        // Wander but stay inside civilization territory
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                // Allow wandering only within the civilization territory
                return TerritoryManager.isInsideCivilization(getX(), getY(), getZ(), CivilizationManager.getCivilization(this.mob));
            }
        });
    }
}
