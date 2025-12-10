package net.reminitous.civilizationsmod.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TerritorySavedData extends SavedData {

    // Map of chunk positions to owning civilization IDs
    private final Map<ChunkPos, CivilizationRecord> territoryMap = new HashMap<>();

    private static final String DATA_NAME = "civilization_territory";

    public TerritorySavedData() {
        super(DATA_NAME);
    }

    /**
     * Get the singleton instance for a given level.
     */
    public static TerritorySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                nbt -> new TerritorySavedData(), // loader function if none exists
                DATA_NAME
        );
    }

    /**
     * Returns the owner of the chunk if it exists.
     */
    public Optional<CivilizationRecord> getCivForChunk(ChunkPos pos) {
        return Optional.ofNullable(territoryMap.get(pos));
    }

    /**
     * Assign a chunk to a civilization.
     */
    public void setCivForChunk(ChunkPos pos, CivilizationRecord civ) {
        territoryMap.put(pos, civ);
        setDirty(); // mark for saving
    }

    /**
     * Remove a chunk ownership
     */
    public void removeChunk(ChunkPos pos) {
        territoryMap.remove(pos);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        // Simple NBT serialization
        // For example, store as "chunk_x_z" -> civilization ID
        for (Map.Entry<ChunkPos, CivilizationRecord> entry : territoryMap.entrySet()) {
            String key = entry.getKey().x + "_" + entry.getKey().z;
            tag.putString(key, entry.getValue().id);
        }
        return tag;
    }
}
