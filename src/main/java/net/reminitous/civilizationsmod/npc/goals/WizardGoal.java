package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.EnumSet;
import java.util.List;

public class WizardGoal extends Goal {

    private final Monster wizard;

    public WizardGoal(Monster wizard) {
        this.wizard = wizard;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(wizard.getX(), wizard.getY(), wizard.getZ(),
                CivilizationManager.getCivilization(wizard));
    }

    @Override
    public void tick() {
        AABB area = TerritoryManager.getBoundingBoxForCivilization(CivilizationManager.getCivilization(wizard));

        List<Player> intruders = wizard.level().getEntitiesOfClass(Player.class, area);
        for (Player p : intruders) {
            if (!CivilizationManager.isAlly(wizard, p)) {
                // Apply curse or attack logic
                wizard.setTarget(p);
            }
        }

        // Wizard could also brew potions from civilization storage periodically
    }
}
