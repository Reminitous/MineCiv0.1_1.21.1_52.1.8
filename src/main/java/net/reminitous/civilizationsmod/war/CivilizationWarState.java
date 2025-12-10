package net.reminitous.civilizationsmod.civilization;

import java.util.UUID;

public class CivilizationWarState {

    private final UUID attacker;
    private final UUID defender;

    private boolean warActive;
    private long warStartTime;
    private long warEndTime;

    private int attackerHealth;
    private int defenderHealth;

    // Constructor
    public CivilizationWarState(UUID attacker, UUID defender, int attackerHealth, int defenderHealth) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackerHealth = attackerHealth;
        this.defenderHealth = defenderHealth;
        this.warActive = true;
        this.warStartTime = System.currentTimeMillis();
        this.warEndTime = -1;
    }

    // ===========================
    // WAR STATE QUERIES
    // ===========================

    /** Returns true if the war is active. */
    public boolean isWarActive() {
        return warActive;
    }

    /** Returns true if the war has ended. */
    public boolean isWarOver() {
        return !warActive;
    }

    /** Returns true if either side has been defeated. */
    public boolean isEitherSideDefeated() {
        return attackerHealth <= 0 || defenderHealth <= 0;
    }

    // ===========================
    // WAR STATE MUTATORS
    // ===========================

    public void endWar() {
        this.warActive = false;
        this.warEndTime = System.currentTimeMillis();
    }

    public void damageAttacker(int amount) {
        attackerHealth -= amount;
        if (attackerHealth <= 0) {
            endWar();
        }
    }

    public void damageDefender(int amount) {
        defenderHealth -= amount;
        if (defenderHealth <= 0) {
            endWar();
        }
    }

    // ===========================
    // GETTERS
    // ===========================

    public UUID getAttacker() {
        return attacker;
    }

    public UUID getDefender() {
        return defender;
    }

    public int getAttackerHealth() {
        return attackerHealth;
    }

    public int getDefenderHealth() {
        return defenderHealth;
    }

    public long getWarStartTime() {
        return warStartTime;
    }

    public long getWarEndTime() {
        return warEndTime;
    }
}
