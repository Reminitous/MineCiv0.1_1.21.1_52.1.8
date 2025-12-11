package net.reminitous.civilizationsmod.systems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.CivilizationData;
import net.reminitous.civilizationsmod.utils.ItemUtils;

import java.util.List;

/**
 * Handles spoils of war and resource transfers between civilizations.
 */
public class SpoilsSystem {

    /**
     * Transfers a percentage of the loser's stored items to the winner.
     *
     * @param loser The civilization that lost the war
     * @param winner The civilization that won the war
     * @param percentage The percentage (0-100) of items to transfer
     */
    public static void transferLoot(CivilizationData loser, CivilizationData winner, double percentage) {
        List<ItemStack> loserInventory = loser.getAllStorageItems();

        for (ItemStack stack : loserInventory) {
            int transferAmount = (int) (stack.getCount() * (percentage / 100.0));
            if (transferAmount <= 0) continue;

            ItemStack transferStack = stack.copy();
            transferStack.setCount(transferAmount);
            winner.addToStorage(transferStack);

            stack.setCount(stack.getCount() - transferAmount);
        }

        loser.sendMessageToMembers("You lost a war! " + percentage + "% of your resources have been transferred to " + winner.getName());
        winner.sendMessageToMembers("Victory! You received " + percentage + "% of " + loser.getName() + "'s resources.");
    }

    /**
     * Applies a temporary production penalty to the losing civilization.
     *
     * Example: if a technology civ loses 20% production, every mined cobblestone or smelted item is split.
     *
     * @param loser The civilization that lost
     * @param winner The winning civilization
     * @param productionPercentage The percentage of production lost (0-100)
     */
    public static void applyProductionPenalty(CivilizationData loser, CivilizationData winner, double productionPercentage) {
        loser.setProductionPenalty(productionPercentage);
        winner.sendMessageToMembers("You are now receiving " + productionPercentage + "% of " + loser.getName() + "'s production.");
    }

    /**
     * Resets production penalties (should be called after the war grace period ends).
     *
     * @param civ The civilization to reset
     */
    public static void resetProductionPenalty(CivilizationData civ) {
        civ.setProductionPenalty(0);
        civ.sendMessageToMembers("Your production penalty has ended.");
    }

    /**
     * Helper method to calculate the "value" of items for war health calculations.
     * Can be used by WarSystem.
     */
    public static int calculateInventoryValue(CivilizationData civ) {
        int totalValue = 0;
        for (ItemStack stack : civ.getAllStorageItems()) {
            int itemValue = ItemUtils.getItemValue(stack);
            totalValue += itemValue * stack.getCount();
        }
        return totalValue;
    }
}
