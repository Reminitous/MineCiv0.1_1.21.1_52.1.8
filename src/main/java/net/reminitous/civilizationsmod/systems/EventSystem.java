package net.reminitous.civilizationsmod.systems;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.reminitous.civilizationsmod.civilization.CivilizationData;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.utils.ItemUtils;

import java.util.Random;

/**
 * Handles quests, random events, and civilization-wide XP events.
 */
public class EventSystem {

    private static final Random random = new Random();

    /**
     * Triggers a random event for a civilization.
     * Events vary depending on civilization class.
     *
     * @param civ The civilization to trigger the event for
     * @param world The world the civilization exists in
     */
    public static void triggerRandomEvent(CivilizationData civ, Level world) {
        switch (civ.getCivClass()) {
            case AGRICULTURAL -> triggerAgriculturalEvent(civ, world);
            case WARLIKE -> triggerWarlikeEvent(civ, world);
            case TECHNOLOGY -> triggerTechnologyEvent(civ, world);
            case MYSTIC -> triggerMysticEvent(civ, world);
            case MERCHANT -> triggerMerchantEvent(civ, world);
        }
    }

    private static void triggerAgriculturalEvent(CivilizationData civ, Level world) {
        // Example: crop pest event
        if (random.nextFloat() < 0.3) { // 30% chance
            civ.addXP(50);
            civ.sendMessageToMembers("Your crops have been successfully harvested with extra yield! +50 XP");
        } else {
            civ.sendMessageToMembers("A crop pest has destroyed some of your harvest!");
            // Logic to reduce crop blocks in territory
        }
    }

    private static void triggerWarlikeEvent(CivilizationData civ, Level world) {
        // Example: a band of hostile NPCs spawn near territory
        civ.addXP(40);
        civ.sendMessageToMembers("A hostile raid is approaching! +40 XP");
        // Logic to spawn hostile NPCs near the civilization
    }

    private static void triggerTechnologyEvent(CivilizationData civ, Level world) {
        civ.addXP(30);
        civ.sendMessageToMembers("Your engineers have optimized a furnace! +30 XP");
        // Logic to apply temporary smelting speed boost
    }

    private static void triggerMysticEvent(CivilizationData civ, Level world) {
        civ.addXP(35);
        civ.sendMessageToMembers("Your wizards discovered a new magical recipe! +35 XP");
        // Logic to give temporary potion/enchanting bonus
    }

    private static void triggerMerchantEvent(CivilizationData civ, Level world) {
        civ.addXP(25);
        civ.sendMessageToMembers("A lucrative trade opportunity arises! +25 XP");
        // Logic to generate extra resources or coins (if implemented)
    }

    /**
     * Triggers civilization-wide XP gain for common actions.
     *
     * @param civ The civilization
     * @param value The value of the action (ore mined, mob killed, crop harvested)
     */
    public static void handleXPGain(CivilizationData civ, int value) {
        int xpGained = Math.max(1, value / 10); // Example: scale down item value to XP
        civ.addXP(xpGained);
    }

    /**
     * Sends periodic random events to all civilizations.
     */
    public static void tickRandomEvents() {
        for (CivilizationData civ : CivilizationManager.getAllCivilizations()) {
            if (random.nextFloat() < 0.05) { // 5% chance per tick to trigger event
                triggerRandomEvent(civ, civ.getWorld());
            }
        }
    }
}
