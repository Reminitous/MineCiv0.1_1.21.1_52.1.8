package net.reminitous.civilizationsmod.data;

import net.minecraft.nbt.CompoundTag;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Basic civilization record stored inside TerritorySavedData.
 */
public class CivilizationRecord {
    public final String id;
    public String displayName;
    public UUID leader;
    public long lastActiveMillis;
    public final Set<Long> claimedChunks = new HashSet<>();
    public long monumentChunkLong = Long.MIN_VALUE; // chunk of the monument

    public CivilizationRecord(String id, String displayName, UUID leader, long placedAtMillis, long monumentChunkLong) {
        this.id = id;
        this.displayName = displayName;
        this.leader = leader;
        this.lastActiveMillis = placedAtMillis;
        this.monumentChunkLong = monumentChunkLong;
        // claim the monument chunk by default
        if (monumentChunkLong != Long.MIN_VALUE) claimedChunks.add(monumentChunkLong);
    }

    public CompoundTag toTag() {
        CompoundTag t = new CompoundTag();
        t.putString("id", id);
        t.putString("displayName", displayName);
        t.putUUID("leader", leader);
        t.putLong("lastActiveMillis", lastActiveMillis);
        t.putLong("monumentChunk", monumentChunkLong);
        long[] arr = claimedChunks.stream().mapToLong(Long::longValue).toArray();
        t.putLongArray("chunks", arr);
        return t;
    }

    public static CivilizationRecord fromTag(CompoundTag t) {
        String id = t.getString("id");
        String name = t.getString("displayName");
        UUID leader = t.hasUUID("leader") ? t.getUUID("leader") : new UUID(0,0);
        long lastActive = t.getLong("lastActiveMillis");
        long monumentChunk = t.getLong("monumentChunk");
        CivilizationRecord r = new CivilizationRecord(id, name, leader, lastActive, monumentChunk);
        long[] arr = t.getLongArray("chunks");
        for (long l : arr) r.claimedChunks.add(l);
        return r;
    }
}
