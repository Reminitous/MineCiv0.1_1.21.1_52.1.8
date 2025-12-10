package net.reminitous.civilizationsmod.civilization;

import net.reminitous.civilizationsmod.war.CivilizationWarState;

import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Core class representing a civilization.
 */
public class Civilization {

    private final String name;
    private String leader;
    private final CivilizationClass civilizationClass;
    private final Set<String> members = new HashSet<>();
    private final Set<String> allies = new HashSet<>();
    private final Set<String> rivals = new HashSet<>();
    private final Map<ItemStack, Integer> storedItems = new HashMap<>();
    private final Set<String> territoryChunks = new HashSet<>();
    private int virtualLevel;
    private CivilizationWarState currentWar;

    // Constructor
    public Civilization(String name, String leader, CivilizationClass civilizationClass) {
        this.name = name;
        this.leader = leader;
        this.civilizationClass = civilizationClass;
        this.members.add(leader);
        this.virtualLevel = 1;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getLeader() { return leader; }
    public void setLeader(String leader) {
        if (members.contains(leader)) {
            this.leader = leader;
        }
    }

    public CivilizationClass getCivilizationClass() { return civilizationClass; }

    public Set<String> getMembers() { return members; }
    public boolean addMember(String playerName) {
        if (members.size() < 10) { // Max members
            return members.add(playerName);
        }
        return false;
    }
    public boolean removeMember(String playerName) {
        if (members.contains(playerName) && !playerName.equals(leader)) {
            return members.remove(playerName);
        }
        return false;
    }

    public Set<String> getAllies() { return allies; }
    public boolean addAlly(String civName) { return allies.add(civName); }
    public boolean removeAlly(String civName) { return allies.remove(civName); }

    public Set<String> getRivals() { return rivals; }
    public boolean addRival(String civName) { return rivals.add(civName); }
    public boolean removeRival(String civName) { return rivals.remove(civName); }

    public Map<ItemStack, Integer> getStoredItems() { return storedItems; }
    public void addItems(ItemStack item, int amount) {
        storedItems.put(item, storedItems.getOrDefault(item, 0) + amount);
    }
    public void removeItems(ItemStack item, int amount) {
        storedItems.put(item, Math.max(0, storedItems.getOrDefault(item, 0) - amount));
    }

    public Set<String> getTerritoryChunks() { return territoryChunks; }
    public boolean addChunk(String chunkId) {
        if (territoryChunks.size() < 100) { // Max 10x10 chunks
            return territoryChunks.add(chunkId);
        }
        return false;
    }
    public boolean removeChunk(String chunkId) { return territoryChunks.remove(chunkId); }

    public int getVirtualLevel() { return virtualLevel; }
    public void setVirtualLevel(int virtualLevel) { this.virtualLevel = virtualLevel; }

    public CivilizationWarState getCurrentWar() { return currentWar; }
    public void setCurrentWar(CivilizationWarState war) { this.currentWar = war; }

    // Utility methods
    public boolean isMember(String playerName) {
        return members.contains(playerName);
    }

    public boolean isLeader(String playerName) {
        return leader.equals(playerName);
    }

    public boolean isInWar() {
        return currentWar != null && !currentWar.isWarOver();
    }

    public boolean canAttack(String playerName, String chunkOwnerCivilization) {
        if (members.contains(playerName) && !members.contains(chunkOwnerCivilization)) {
            return true;
        }
        return false;
    }

    public int getMemberCount() {
        return members.size();
    }
}
