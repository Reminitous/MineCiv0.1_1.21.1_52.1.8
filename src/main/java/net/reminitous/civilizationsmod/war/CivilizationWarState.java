package net.reminitous.civilizationsmod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class CivilizationWarState {

    private final String attacker;
    private final String defender;

    private double attackerHealth;
    private double defenderHealth;

    private final Set<ServerPlayer> attackerMembers = new HashSet<>();
    private final Set<ServerPlayer> defenderMembers = new HashSet<>();

    private final Set<BlockPos> attackerTerritory = new HashSet<>();
    private final Set<BlockPos> defenderTerritory = new HashSet<>();

    private boolean warEnded = false;

    public CivilizationWarState(String attacker, String defender,
                                Set<ServerPlayer> attackerMembers, Set<ServerPlayer> defenderMembers,
                                Set<BlockPos> attackerTerritory, Set<BlockPos> defenderTerritory) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackerMembers.addAll(attackerMembers);
        this.defenderMembers.addAll(defenderMembers);
        this.attackerTerritory.addAll(attackerTerritory);
        this.defenderTerritory.addAll(defenderTerritory);

        this.attackerHealth = calculateCivilizationHealth(attackerTerritory);
        this.defenderHealth = calculateCivilizationHealth(defenderTerritory);
    }

    // Example health calculation based on chest contents
    private double calculateCivilizationHealth(Set<BlockPos> territory) {
        double totalValue = 0;
        for (BlockPos pos : territory) {
            // Placeholder: iterate chests in territory and sum item values
            totalValue += 100; // simplified for demonstration
        }
        return totalValue;
    }

    public void damageAttacker(double amount) {
        attackerHealth -= amount;
        checkEnd();
    }

    public void damageDefender(double amount) {
        defenderHealth -= amount;
        checkEnd();
    }

    private void checkEnd() {
        if (attackerHealth <= 0 || defenderHealth <= 0) {
            warEnded = true;
        }
    }

    public boolean hasEnded() {
        return warEnded;
    }

    public String getWinner() {
        if (!warEnded) return null;
        return attackerHealth > defenderHealth ? attacker : defender;
    }

    public String getLoser() {
        if (!warEnded) return null;
        return attackerHealth > defenderHealth ? defender : attacker;
    }

    public Set<ServerPlayer> getWinnerMembers() {
        return getWinner().equals(attacker) ? attackerMembers : defenderMembers;
    }

    public Set<ServerPlayer> getLoserMembers() {
        return getLoser().equals(attacker) ? attackerMembers : defenderMembers;
    }

    public Set<BlockPos> getWinnerTerritory() {
        return getWinner().equals(attacker) ? attackerTerritory : defenderTerritory;
    }

    public Set<BlockPos> getLoserTerritory() {
        return getLoser().equals(attacker) ? attackerTerritory : defenderTerritory;
    }
}
