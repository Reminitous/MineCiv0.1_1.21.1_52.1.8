package net.reminitous.civilizationsmod.territory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.reminitous.civilizationsmod.civilization.CivilizationData;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single chunk claimed by a civilization.
 * Stores the owner civilization, list of NPCs, and any special flags.
 */
public class TerritoryChunkData {

    private final ChunkPos chunkPos;           // Chunk coordinates
    private CivilizationData ownerCiv;         // Owning civilization
    private Set<String> npcIds;                 // IDs of NPCs spawned in this chunk
    private boolean isUnderWar;                 // Whether this chunk is in active war
    private long lastActiveMillis;              // Last time a member of the civ was online

    public TerritoryChunkData(ChunkPos pos, CivilizationData owner) {
        this.chunkPos = pos;
        this.ownerCiv = owner;
        this.npcIds = new HashSet<>();
        this.isUnderWar = false;
        this.lastActiveMillis = System.currentTimeMillis();
    }

    // Getter & Setter

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public CivilizationData getOwnerCiv() {
        return ownerCiv;
    }

    public void setOwnerCiv(CivilizationData ownerCiv) {
        this.ownerCiv = ownerCiv;
    }

    public Set<String> getNpcIds() {
        return npcIds;
    }

    public void addNpcId(String npcId) {
        npcIds.add(npcId);
    }

    public void removeNpcId(String npcId) {
        npcIds.remove(npcId);
    }

    public boolean isUnderWar() {
        return isUnderWar;
    }

    public void setUnderWar(boolean underWar) {
        isUnderWar = underWar;
    }

    public long getLastActiveMillis() {
        return lastActiveMillis;
    }

    public void updateLastActive() {
        this.lastActiveMillis = System.currentTimeMillis();
    }

    // Convenience method to check if monument should rot (1 week of inactivity)
    public boolean shouldRot() {
        long oneWeekMillis = 7L * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastActiveMillis >= oneWeekMillis;
    }

    @Override
    public String toString() {
        return "TerritoryChunkData{" +
                "chunkPos=" + chunkPos +
                ", ownerCiv=" + (ownerCiv != null ? ownerCiv.getName() : "None") +
                ", npcCount=" + npcIds.size() +
                ", underWar=" + isUnderWar +
                '}';
    }
}
