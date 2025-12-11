package net.reminitous.civilizationsmod.data;

public class CivilizationRecord {
    public final String id;
    public final String displayName;

    // ADD THESE:
    public long lastActiveMillis;
    public long monumentChunkLong = Long.MIN_VALUE;
    public final java.util.Set<net.minecraft.world.level.ChunkPos> claimedChunks = new java.util.HashSet<>();

    public CivilizationRecord(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.lastActiveMillis = System.currentTimeMillis();
    }

    // ADD GETTERS:
    public String getId() { return id; }
    public long getLastActiveMillis() { return lastActiveMillis; }
    public net.minecraft.core.BlockPos getMonumentPos() {
        if (monumentChunkLong == Long.MIN_VALUE) return null;
        net.minecraft.world.level.ChunkPos cp = new net.minecraft.world.level.ChunkPos(monumentChunkLong);
        return new net.minecraft.core.BlockPos(
                (cp.getMinBlockX() + cp.getMaxBlockX()) / 2,
                64,
                (cp.getMinBlockZ() + cp.getMaxBlockZ()) / 2
        );
    }
}