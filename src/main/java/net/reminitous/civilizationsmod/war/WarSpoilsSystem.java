package net.reminitous.civilizationsmod.war;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.systems.CivilizationStorage;

public class WarSpoilsSystem {

    /**
     * Transfer a percentage of lost civilization resources to the winner.
     */
    public static void transferSpoils(Level level, Civilization loser, Civilization winner, double percentage) {
        CivilizationStorage loserStorage = new CivilizationStorage(loser);
        CivilizationStorage winnerStorage = new CivilizationStorage(winner);

        loserStorage.getAllContainers(level).forEach(container -> {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty()) {
                    int amountToTransfer = (int) Math.floor(stack.getCount() * percentage);
                    if (amountToTransfer > 0) {
                        ItemStack toTransfer = stack.split(amountToTransfer);
                        winnerStorage.depositItem(level, toTransfer);
                    }
                }
            }
        });
    }

    /**
     * Apply temporary resource deduction from loser’s production after losing war.
     * E.g., 20% of resources go to winner for X time.
     */
    public static void applyProductionPenalty(Level level, Civilization loser, Civilization winner, double percentage, long durationTicks) {
        // This can be implemented with a scheduled task system or event listener
        // Here we just store the percentage mapping for integration with NPC production logic
        loser.setProductionPenalty(winner, percentage, durationTicks);
    }
}
