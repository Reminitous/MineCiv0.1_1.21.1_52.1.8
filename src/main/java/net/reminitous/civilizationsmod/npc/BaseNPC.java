package net.reminitous.civilizationsmod.npc;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base class for all custom NPCs in the Civilizations Mod.
 */
public abstract class BaseNPC extends Mob {

    public BaseNPC(Level level) {
        super(null, level); // Replace null with actual EntityType when registering entities
    }

    /**
     * Add goals specific to this NPC.
     */
    protected abstract void registerGoals();

    /**
     * Optional: Add common NPC behaviors here.
     */
    @Override
    public void tick() {
        super.tick();
        // Could add common AI tasks for all NPCs here
    }

    // You can add common helper methods for NPCs here
}
