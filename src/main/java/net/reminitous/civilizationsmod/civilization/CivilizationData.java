package net.reminitous.civilizationsmod.Civilization;

import net.minecraft.world.level.ChunkPos;
import java.util.*;

public class CivilizationData {

    private String name;
    private UUID leader;
    private CivilizationClass civClass;
    private Set<UUID> members = new HashSet<>();
    private Set<ChunkPos> territory = new HashSet<>();
    private int virtualLevel = 1;
    private long experience = 0;
    private long lastActiveTimestamp;

    private Set<UUID> allies = new HashSet<>();
    private Set<UUID> rivals = new HashSet<>();

    public CivilizationData(String name, UUID leader, CivilizationClass civClass) {
        this.name = name;
        this.leader = leader;
        this.civClass = civClass;
        this.members.add(leader);
        this.lastActiveTimestamp = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeader() {
        return leader;
    }

    public CivilizationClass getCivClass() {
        return civClass;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean addMember(UUID playerId) {
        if (members.size() >= 10) return false; // max members
        return members.add(playerId);
    }

    public boolean removeMember(UUID playerId) {
        if (playerId.equals(leader)) return false; // cannot remove leader
        return members.remove(playerId);
    }

    public Set<ChunkPos> getTerritory() {
        return territory;
    }

    public void addChunk(ChunkPos chunkPos) {
        if (territory.size() < 100) { // max 10x10 chunks
            territory.add(chunkPos);
        }
    }

    public long getExperience() {
        return experience;
    }

    public void addExperience(long amount) {
        this.experience += amount;
        updateVirtualLevel();
    }

    private void updateVirtualLevel() {
        this.virtualLevel = (int) (experience / 1000) + 1; // simple leveling logic
    }

    public int getVirtualLevel() {
        return virtualLevel;
    }

    public void updateLastActive() {
        this.lastActiveTimestamp = System.currentTimeMillis();
    }

    public long getLastActiveTimestamp() {
        return lastActiveTimestamp;
    }

    public Set<UUID> getAllies() {
        return allies;
    }

    public Set<UUID> getRivals() {
        return rivals;
    }

    public boolean isMember(UUID playerId) {
        return members.contains(playerId);
    }
}
