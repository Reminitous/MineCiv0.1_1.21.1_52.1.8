package net.reminitous.civilizationsmod.systems;

import net.reminitous.civilizationsmod.civilization.CivilizationData;
import net.reminitous.civilizationsmod.territory.TerritoryManager;
import java.util.*;

public class WarSystem {

    private static final Map<String, WarState> wars = new HashMap<>();

    public static void proposeWar(String attacker, String defender, int prepMinutes) {
        wars.put(defender, new WarState(attacker, defender, prepMinutes));
    }

    public static void acceptWar(String defender) {
        WarState state = wars.get(defender);
        if (state != null) state.beginCountdown();
    }

    public static void tick() {
        wars.values().forEach(WarState::tick);
    }

    public static class WarState {
        public String attacker, defender;
        public long startTime;
        public int prepMinutes;
        public boolean started = false;

        public WarState(String a, String d, int prep) {
            attacker = a;
            defender = d;
            prepMinutes = prep;
            startTime = System.currentTimeMillis();
        }

        public void beginCountdown() {
            startTime = System.currentTimeMillis();
        }

        public void tick() {
            long now = System.currentTimeMillis();
            if (!started && now - startTime >= prepMinutes * 60000L) {
                started = true;
                System.out.println("War started between " + attacker + " and " + defender);
                // Initialize health, durability buffs, etc.
            }
        }

        public void resolve(CivilizationData loser, CivilizationData winner) {
            // Transfer spoils
            // Apply production penalty
            // Set grace periods
        }
    }
}
