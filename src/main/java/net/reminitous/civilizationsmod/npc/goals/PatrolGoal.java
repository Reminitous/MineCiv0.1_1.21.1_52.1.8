package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.EnumSet;
import java.util.List;

public class PatrolGoal extends Goal {

    private final Monster patroller;

    public PatrolGoal(Monster patroller) {
        this.patroller = patroller;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(patroller.getX(), patroller.getY(), patroller.getZ(),
                CivilizationManager.getCivilization(patroller));
    }

    @Override
    public void tick() {
        AABB area = TerritoryManager.getBoundingBoxForCivilization(CivilizationManager.getCivilization(patroller));

        List<Player> intruders = patroller.level().getEntitiesOfClass(Player.class, area);
        for (Player p : intruders) {
            if (!CivilizationManager.isAlly(patroller, p)) {
                patroller.setTarget(p); // attack intruder
            }
        }
    }
}
