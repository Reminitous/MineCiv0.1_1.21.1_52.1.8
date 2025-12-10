package net.reminitous.civilizationsmod.war;

import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.systems.CivilizationStorage;

public class WarMechanics {

    /**
     * Calculate the total health of a civilization based on its storage.
     */
    public static long calculateCivilizationHealth(Level level, Civilization civ) {
        CivilizationStorage storage = new CivilizationStorage(civ);
        return storage.getTotalValue(level);
    }

    /**
     * Apply damage to a civilization during war.
     * The damage reduces stored items proportionally.
     */
    public static void applyDamage(Level level, Civilization civ, long damage) {
        CivilizationStorage storage = new CivilizationStorage(civ);
        long totalValue = storage.getTotalValue(level);
        if (totalValue <= 0) return;

        double proportion = (double) damage / totalValue;

        // Iterate through all containers and reduce items proportionally
        storage.getAllContainers(level).forEach(container -> {
            for (int i = 0; i < container.getContainerSize(); i++) {
                var stack = container.getItem(i);
                if (!stack.isEmpty()) {
                    int reduce = (int) Math.ceil(stack.getCount() * proportion);
                    stack.shrink(reduce);
                }
            }
        });
    }
}
