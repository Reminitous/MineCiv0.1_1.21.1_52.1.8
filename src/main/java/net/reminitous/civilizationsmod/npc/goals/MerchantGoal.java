package net.reminitous.civilizationsmod.npc.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.EnumSet;

public class MerchantGoal extends Goal {

    private final Villager merchant;

    public MerchantGoal(Villager merchant) {
        this.merchant = merchant;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return TerritoryManager.isInsideCivilization(merchant.getX(), merchant.getY(), merchant.getZ(),
                CivilizationManager.getCivilization(merchant));
    }

    @Override
    public void tick() {
        BlockPos pos = merchant.blockPosition();
        // Merchant logic: trade with players or transfer items between civilization storage
    }
}
