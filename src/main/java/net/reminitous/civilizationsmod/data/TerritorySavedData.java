package net.reminitous.civilizationsmod.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;

import java.util.*;

/**
 * SavedData implementation for storing chunk ownership and territory data.
 * This persists between server restarts.
 */
public class TerritorySavedData extends SavedData {

    private static final String DATA_NAME = "civilizations_territory";

    // Map of chunk position to civilization UUID
    private final Map<ChunkPos, UUID> chunkOwnership = new HashMap<>();

    /**
     * Create new empty territory data.
     */
    public TerritorySavedData() {
        super();
    }

    /**
     * Get the SavedData instance for a level.
     * Automatically loads or creates new data.
     *
     * @param level The server level
     * @return The territory data for this level
     */
    public static TerritorySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(
                        TerritorySavedData::new,
                        TerritorySavedData::load,
                        null
                ),
                DATA_NAME
        );
    }

    /**
     * Load territory data from NBT.
     *
     * @param tag The NBT tag containing saved data
     * @param lookupProvider Holder lookup provider (required in 1.21.1)
     * @return Loaded TerritorySavedData
     */
    public static TerritorySavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        TerritorySavedData data = new TerritorySavedData();

        // Load chunk ownership
        if (tag.contains("ChunkOwnership", Tag.TAG_LIST)) {
            ListTag chunkList = tag.getList("ChunkOwnership", Tag.TAG_COMPOUND);

            for (int i = 0; i < chunkList.size(); i++) {
                CompoundTag chunkTag = chunkList.getCompound(i);

                int chunkX = chunkTag.getInt("ChunkX");
                int chunkZ = chunkTag.getInt("ChunkZ");
                UUID civId = chunkTag.getUUID("CivId");

                ChunkPos pos = new ChunkPos(chunkX, chunkZ);
                data.chunkOwnership.put(pos, civId);
            }
        }

        return data;
    }

    /**
     * Save territory data to NBT.
     *
     * @param tag The tag to write to
     * @param lookupProvider Holder lookup provider (required in 1.21.1)
     * @return The populated NBT tag
     */
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        // Save chunk ownership
        ListTag chunkList = new ListTag();

        for (Map.Entry<ChunkPos, UUID> entry : chunkOwnership.entrySet()) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putInt("ChunkX", entry.getKey().x);
            chunkTag.putInt("ChunkZ", entry.getKey().z);
            chunkTag.putUUID("CivId", entry.getValue());
            chunkList.add(chunkTag);
        }

        tag.put("ChunkOwnership", chunkList);

        return tag;
    }

    // ===========================
    // CHUNK OWNERSHIP QUERIES
    // ===========================

    /**
     * Get the civilization that owns a chunk.
     *
     * @param chunkPos The chunk position
     * @return Optional containing the owning civilization
     */
    public Optional<Civilization> getCivilizationForChunk(ChunkPos chunkPos) {
        UUID civId = chunkOwnership.get(chunkPos);
        if (civId == null) {
            return Optional.empty();
        }

        Civilization civ = CivilizationManager.getCivilization(civId);
        return Optional.ofNullable(civ);
    }

    /**
     * Get the civilization UUID that owns a chunk.
     *
     * @param chunkPos The chunk position
     * @return The civilization UUID, or null if unclaimed
     */
    public UUID getCivilizationIdForChunk(ChunkPos chunkPos) {
        return chunkOwnership.get(chunkPos);
    }

    /**
     * Check if a chunk is claimed by any civilization.
     *
     * @param chunkPos The chunk position
     * @return true if the chunk is claimed
     */
    public boolean isChunkClaimed(ChunkPos chunkPos) {
        return chunkOwnership.containsKey(chunkPos);
    }

    /**
     * Get all chunks owned by a specific civilization.
     *
     * @param civId The civilization UUID
     * @return Set of chunks owned by this civilization
     */
    public Set<ChunkPos> getChunksOwnedBy(UUID civId) {
        Set<ChunkPos> chunks = new HashSet<>();

        for (Map.Entry<ChunkPos, UUID> entry : chunkOwnership.entrySet()) {
            if (entry.getValue().equals(civId)) {
                chunks.add(entry.getKey());
            }
        }

        return chunks;
    }

    /**
     * Get all claimed chunks.
     *
     * @return Set of all claimed chunk positions
     */
    public Set<ChunkPos> getAllClaimedChunks() {
        return new HashSet<>(chunkOwnership.keySet());
    }

    // ===========================
    // CHUNK OWNERSHIP MUTATIONS
    // ===========================

    /**
     * Set a chunk's owner.
     *
     * @param chunkPos The chunk position
     * @param civilization The owning civilization
     */
    public void setChunkOwner(ChunkPos chunkPos, Civilization civilization) {
        chunkOwnership.put(chunkPos, civilization.getId());
        setDirty();
    }

    /**
     * Set a chunk's owner by UUID.
     *
     * @param chunkPos The chunk position
     * @param civId The civilization UUID
     */
    public void setChunkOwner(ChunkPos chunkPos, UUID civId) {
        chunkOwnership.put(chunkPos, civId);
        setDirty();
    }

    /**
     * Remove a chunk's ownership.
     *
     * @param chunkPos The chunk position
     */
    public void removeChunkOwner(ChunkPos chunkPos) {
        chunkOwnership.remove(chunkPos);
        setDirty();
    }

    /**
     * Remove all chunks owned by a civilization.
     * Used when a civilization is deleted.
     *
     * @param civId The civilization UUID
     */
    public void removeAllChunksOwnedBy(UUID civId) {
        chunkOwnership.entrySet().removeIf(entry -> entry.getValue().equals(civId));
        setDirty();
    }

    /**
     * Transfer chunk ownership from one civilization to another.
     *
     * @param chunkPos The chunk position
     * @param newOwnerId The new owner's civilization UUID
     * @return true if transferred, false if chunk wasn't owned
     */
    public boolean transferChunkOwnership(ChunkPos chunkPos, UUID newOwnerId) {
        if (!chunkOwnership.containsKey(chunkPos)) {
            return false;
        }

        chunkOwnership.put(chunkPos, newOwnerId);
        setDirty();
        return true;
    }

    // ===========================
    // UTILITY METHODS
    // ===========================

    /**
     * Get the total number of claimed chunks.
     *
     * @return Total claimed chunk count
     */
    public int getTotalClaimedChunks() {
        return chunkOwnership.size();
    }

    /**
     * Get the number of chunks owned by a civilization.
     *
     * @param civId The civilization UUID
     * @return Number of chunks owned
     */
    public int getChunkCountFor(UUID civId) {
        int count = 0;
        for (UUID owner : chunkOwnership.values()) {
            if (owner.equals(civId)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Clear all chunk ownership data.
     * WARNING: This removes all territory claims!
     */
    public void clearAllChunks() {
        chunkOwnership.clear();
        setDirty();
    }

    /**
     * Get statistics about territory distribution.
     *
     * @return Map of civilization UUID to chunk count
     */
    public Map<UUID, Integer> getTerritoryStats() {
        Map<UUID, Integer> stats = new HashMap<>();

        for (UUID civId : chunkOwnership.values()) {
            stats.put(civId, stats.getOrDefault(civId, 0) + 1);
        }

        return stats;
    }

    // ===========================
    // DEBUGGING
    // ===========================

    @Override
    public String toString() {
        return "TerritorySavedData{" +
                "totalChunks=" + chunkOwnership.size() +
                ", civilizations=" + new HashSet<>(chunkOwnership.values()).size() +
                '}';
    }
}