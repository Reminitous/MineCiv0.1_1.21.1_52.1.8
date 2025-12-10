package net.reminitous.civilizationsmod.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SavedData that persists civilizations and claimed chunks.
 * - civilization id -> CivilizationRecord
 * - chunkLong -> civId
 */
public class TerritorySavedData extends SavedData {
    public static final String DATA_KEY = "civilizations_mod:territories_v1";

    private final Map<String, CivilizationRecord> civs = new ConcurrentHashMap<>();
    private final Map<Long, String> chunkToCiv = new ConcurrentHashMap<>();

    public TerritorySavedData() { }

    public static TerritorySavedData load(CompoundTag tag) {
        TerritorySavedData data = new TerritorySavedData();
        if (tag.contains("civs")) {
            CompoundTag civsTag = tag.getCompound("civs");
            for (String key : civsTag.getAllKeys()) {
                CompoundTag cv = civsTag.getCompound(key);
                CivilizationRecord record = CivilizationRecord.fromTag(cv);
                data.civs.put(record.id, record);
                for (Long l : record.claimedChunks) data.chunkToCiv.put(l, record.id);
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag civsTag = new CompoundTag();
        for (CivilizationRecord r : civs.values()) {
            civsTag.put(r.id, r.toTag());
        }
        tag.put("civs", civsTag);
        return tag;
    }

    // --- civ management ---
    public Optional<CivilizationRecord> getCivForChunk(ChunkPos pos) {
        String id = chunkToCiv.get(pos.toLong());
        if (id == null) return Optional.empty();
        return Optional.ofNullable(civs.get(id));
    }

    public CivilizationRecord createCivilization(String id, String displayName, UUID leader, long placedAtMillis, long monumentChunkLong) {
        CivilizationRecord r = new CivilizationRecord(id, displayName, leader, placedAtMillis, monumentChunkLong);
        civs.put(id, r);
        // claim initial chunk (monumentChunkLong already added to record)
        for (Long l : r.claimedChunks) this.chunkToCiv.put(l, id);
        setDirty();
        return r;
    }

    public boolean claimChunk(String civId, ChunkPos pos, int maxChunks) {
        long l = pos.toLong();
        if (chunkToCiv.containsKey(l)) return false;
        CivilizationRecord r = civs.get(civId);
        if (r == null) return false;
        if (r.claimedChunks.size() >= maxChunks) return false;
        r.claimedChunks.add(l);
        chunkToCiv.put(l, civId);
        setDirty();
        return true;
    }

    public void releaseChunk(ChunkPos pos) {
        long l = pos.toLong();
        String id = chunkToCiv.remove(l);
        if (id != null) {
            CivilizationRecord r = civs.get(id);
            if (r != null) r.claimedChunks.remove(l);
            setDirty();
        }
    }

    public CivilizationRecord getById(String id) { return civs.get(id); }
    public Collection<CivilizationRecord> getAll() { return civs.values(); }

    public void removeCivilization(String id) {
        CivilizationRecord r = civs.remove(id);
        if (r != null) {
            for (Long l : r.claimedChunks) chunkToCiv.remove(l);
            setDirty();
        }
    }

    // helper to retrieve from world
    public static TerritorySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                (CompoundTag tag) -> TerritorySavedData.load(tag),
                () -> new TerritorySavedData(),
                DATA_KEY
        );
    }
}
