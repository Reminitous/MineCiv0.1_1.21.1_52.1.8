package net.reminitous.civilizationsmod.systems;

import net.reminitous.civilizationsmod.civilization.CivilizationData;

public class XPSystem {

    public static void addExperience(CivilizationData civ, int xp) {
        civ.experience += xp;
        checkLevelUp(civ);
    }

    private static void checkLevelUp(CivilizationData civ) {
        int needed = civ.virtualLevel * 100; // example scaling
        if (civ.experience >= needed) {
            civ.virtualLevel++;
            civ.experience -= needed;
            civ.territoryRadius = Math.min(10, civ.territoryRadius + 1); // max 10 chunks
        }
    }
}
