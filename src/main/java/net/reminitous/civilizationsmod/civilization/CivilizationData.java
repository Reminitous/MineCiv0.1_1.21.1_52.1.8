package net.reminitous.civilizationsmod.civilization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data container for a single civilization.
 * Includes serialization helpers (to/from NBT) to ease saving/loading.
 *
 * This class is intentionally minimal; extend with quest/progress fields as needed.
 */
public class CivilizationData {
    private String name;
    private UUID leader;
    private CivilizationClass civClass;
    private final Set<UUID> members = new HashSet<>();
    private final Set<ChunkPos> territory = new HashSet<>();
    private final Set<UUID> allies = new HashSet<>();
    private final Set<UUID> rivals = new HashSet<>();

    private int virtualLevel = 1;
    private long experience = 0L;
    private long lastActiveTimestamp = System.currentTimeMillis();

    public CivilizationData(String name, UUID leader, CivilizationClass civClass) {
        this.name = name;
        this.leader = leader;
        this.civClass = civClass;
        this.members.add(leader);
        this.lastActiveTimestamp = System.currentTimeMillis();
    }

    // --------- basic accessors ----------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getLeader() { return leader; }
    public void setLeader(UUID leader) { this.leader = leader; }

    public CivilizationClass getCivClass() { return civClass; }
    public void setCivClass(CivilizationClass civClass) { this.civClass = civClass; }

    public Set<UUID> getMembers() { return members; }
    public Set<ChunkPos> getTerritory() { return territory; }

    public Set<UUID> getAllies() { return allies; }
    public Set<UUID> getRivals() { return rivals; }

    public int getVirtualLevel() { return virtualLevel; }
    public long getExperience() { return experience; }
    public void addExperience(long amount) {
        if (amount <= 0) return;
        this.experience += amount;
        recomputeVirtualLevel();
        this.lastActiveTimestamp = System.currentTimeMillis();
    }

    public long getLastActiveTimestamp() { return lastActiveTimestamp; }
    public void updateLastActive() { this.lastActiveTimestamp = System.currentTimeMillis(); }

    public boolean isMember(UUID playerId) { return members.contains(playerId); }

    // --------- territory helpers ----------
    public boolean claimChunk(ChunkPos pos) {
        return territory.add(pos);
    }

    public boolean unclaimChunk(ChunkPos pos) {
        return territory.remove(pos);
    }

    // --------- simple leveling rule (example) ----------
    private void recomputeVirtualLevel() {
        // Example leveling curve: level = 1 + floor(exp / 1000)
        // Replace this with your desired progression curve and experience sources.
        int newLevel = 1 + (int)(this.experience / 1000L);
        if (newLevel != this.virtualLevel) {
            this.virtualLevel = newLevel;
        }
    }

    // --------- NBT serialization helpers ----------
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putUUID("leader", leader);
        tag.putString("civClass", civClass == null ? "NONE" : civClass.name());
        tag.putInt("virtualLevel", virtualLevel);
        tag.putLong("experience", experience);
        tag.putLong("lastActive", lastActiveTimestamp);

        // members
        CompoundTag membersTag = new CompoundTag();
        int i = 0;
        for (UUID u : members) {
            membersTag.putUUID("m" + i++, u);
        }
        membersTag.putInt("count", i);
        tag.put("members", membersTag);

        // allies/rivals
        CompoundTag alliesTag = new CompoundTag();
        i = 0;
        for (UUID u : allies) alliesTag.putUUID("a" + (i++), u);
        alliesTag.putInt("count", i);
        tag.put("allies", alliesTag);

        CompoundTag rivalsTag = new CompoundTag();
        i = 0;
        for (UUID u : rivals) rivalsTag.putUUID("r" + (i++), u);
        rivalsTag.putInt("count", i);
        tag.put("rivals", rivalsTag);

        // territory as an integer list of (x,z) pairs
        CompoundTag territoryTag = new CompoundTag();
        i = 0;
        for (ChunkPos pos : territory) {
            territoryTag.putInt("x" + i, pos.x);
            territoryTag.putInt("z" + i, pos.z);
            i++;
        }
        territoryTag.putInt("count", i);
        tag.put("territory", territoryTag);

        return tag;
    }

    public static CivilizationData fromNbt(CompoundTag tag) {
        String name = tag.getString("name");
        UUID leader = tag.getUUID("leader");
        CivilizationClass cclass = CivilizationClass.valueOf(tag.getString("civClass"));
        CivilizationData data = new CivilizationData(name, leader, cclass);
        data.virtualLevel = tag.getInt("virtualLevel");
        data.experience = tag.getLong("experience");
        data.lastActiveTimestamp = tag.getLong("lastActive");

        CompoundTag membersTag = tag.getCompound("members");
        int count = membersTag.getInt("count");
        for (int i = 0; i < count; i++) {
            data.members.add(membersTag.getUUID("m" + i));
        }

        CompoundTag alliesTag = tag.getCompound("allies");
        count = alliesTag.getInt("count");
        for (int i = 0; i < count; i++) {
            data.allies.add(alliesTag.getUUID("a" + i));
        }

        CompoundTag rivalsTag = tag.getCompound("rivals");
        count = rivalsTag.getInt("count");
        for (int i = 0; i < count; i++) {
            data.rivals.add(rivalsTag.getUUID("r" + i));
        }

        CompoundTag territoryTag = tag.getCompound("territory");
        count = territoryTag.getInt("count");
        for (int i = 0; i < count; i++) {
            int x = territoryTag.getInt("x" + i);
            int z = territoryTag.getInt("z" + i);
            data.territory.add(new ChunkPos(x, z));
        }

        return data;
    }
}
