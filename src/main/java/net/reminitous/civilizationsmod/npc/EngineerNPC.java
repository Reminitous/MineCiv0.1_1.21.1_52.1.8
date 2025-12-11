package net.reminitous.civilizationsmod.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.npc.goals.EngineerGoal;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

public class EngineerNPC extends Monster {

    public EngineerNPC(EntityType<? extends Monster> type, Level level) {
        super(type, level); // ✅ Pass type, not null
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EngineerGoal(this));

        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return TerritoryManager.isInsideCivilization(getX(), getY(), getZ(), CivilizationManager.getCivilization(this.mob));
            }
        });
    }
}
