package net.reminitous.civilizationsmod.civilization;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the relationships between two civilizations.
 */
public class CivilizationRelation {

    public enum RelationType {
        ALLY,
        RIVAL,
        NEUTRAL,
        TRADE_PARTNER
    }

    private final String civilizationA;
    private final String civilizationB;

    private RelationType relationType;
    private final Set<String> activeTrades = new HashSet<>();

    public CivilizationRelation(String civilizationA, String civilizationB) {
        this.civilizationA = civilizationA;
        this.civilizationB = civilizationB;
        this.relationType = RelationType.NEUTRAL; // default
    }

    // Getters
    public String getCivilizationA() {
        return civilizationA;
    }

    public String getCivilizationB() {
        return civilizationB;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    // Setters
    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    // Trade management
    public void addTrade(String tradeId) {
        activeTrades.add(tradeId);
    }

    public void removeTrade(String tradeId) {
        activeTrades.remove(tradeId);
    }

    public Set<String> getActiveTrades() {
        return activeTrades;
    }

    // Utility methods
    public boolean isAlly() {
        return relationType == RelationType.ALLY;
    }

    public boolean isRival() {
        return relationType == RelationType.RIVAL;
    }

    public boolean isTradePartner() {
        return relationType == RelationType.TRADE_PARTNER;
    }

    public boolean isNeutral() {
        return relationType == RelationType.NEUTRAL;
    }
}
