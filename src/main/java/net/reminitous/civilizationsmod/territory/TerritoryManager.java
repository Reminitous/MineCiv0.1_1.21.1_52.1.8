package net.reminitous.civilizationsmod.territory;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.data.TerritorySavedData;

import java.util.Optional;
import java.util.Random;

/**
 * Central manager for territory-related operations.
 * Handles chunk ownership, territory boundaries, and position queries.
 */
public class TerritoryManager {

    private static final Random RANDOM = new Random();

    /**
     * Load territory data when server starts.
     * Called from main mod class on ServerStartingEvent.
     */
    public static void load(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            TerritorySavedData data = TerritorySavedData.get(level);
            // Data is loaded automatically by SavedData system
        }
    }

    /**
     * Get the TerritorySavedData for a level.
     * This is a convenience wrapper around the SavedData access.
     */
    public static TerritorySavedData get(ServerLevel level) {
        return TerritorySavedData.get(level);
    }

    /**
     * Check if a position (x, y, z) is inside a civilization's territory.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param civ The civilization to check
     * @return true if the position is within the civilization's claimed chunks
     */
    public static boolean isInsideCivilization(double x, double y, double z, Civilization civ) {
        if (civ == null) return false;

        ChunkPos chunkPos = new ChunkPos(new BlockPos((int) x, (int) y, (int) z));
        return civ.getTerritory().contains(chunkPos);
    }

    /**
     * Get the civilization that owns a specific chunk.
     *
     * @param level The server level
     * @param chunkPos The chunk position to check
     * @return Optional containing the owning civilization, or empty if unclaimed
     */
    public static Optional<Civilization> getCivilizationForChunk(ServerLevel level, ChunkPos chunkPos) {
        TerritorySavedData data = get(level);
        return data.getCivilizationForChunk(chunkPos);
    }

    /**
     * Get the civilization ID at a block position.
     *
     * @param pos The block position
     * @return The civilization ID as a string, or null if unclaimed
     */
    public static String getCivilizationAt(BlockPos pos) {
        // This method needs a ServerLevel reference to work properly
        // For now, return null - calling code should use getCivilizationForChunk instead
        return null;
    }

    /**
     * Get a bounding box encompassing all chunks owned by a civilization.
     *
     * @param civ The civilization
     * @return An AABB covering all the civilization's territory
     */
    public static AABB getBoundingBoxForCivilization(Civilization civ) {
        if (civ == null || civ.getTerritory().isEmpty()) {
            return new AABB(0, 0, 0, 0, 0, 0);
        }

        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (ChunkPos chunk : civ.getTerritory()) {
            minX = Math.min(minX, chunk.getMinBlockX());
            minZ = Math.min(minZ, chunk.getMinBlockZ());
            maxX = Math.max(maxX, chunk.getMaxBlockX());
            maxZ = Math.max(maxZ, chunk.getMaxBlockZ());
        }

        // Use world height limits (y: -64 to 320 in 1.21.1)
        return new AABB(minX, -64, minZ, maxX + 1, 320, maxZ + 1);
    }

    /**
     * Get a random position within a civilization's territory.
     * Useful for NPC spawning.
     *
     * @param level The server level
     * @param civ The civilization
     * @return A random BlockPos within the territory, or null if no valid position
     */
    public static BlockPos getRandomPositionInTerritory(ServerLevel level, Civilization civ) {
        if (civ == null || civ.getTerritory().isEmpty()) {
            return null;
        }

        // Get random chunk from territory
        ChunkPos[] chunks = civ.getTerritory().toArray(new ChunkPos[0]);
        ChunkPos randomChunk = chunks[RANDOM.nextInt(chunks.length)];

        // Get random position within that chunk
        int x = randomChunk.getMinBlockX() + RANDOM.nextInt(16);
        int z = randomChunk.getMinBlockZ() + RANDOM.nextInt(16);

        // Find suitable Y coordinate (on ground)
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, x, z);

        return new BlockPos(x, y, z);
    }

    /**
     * Claim a chunk for a civilization.
     *
     * @param level The server level
     * @param chunkPos The chunk to claim
     * @param civ The civilization claiming the chunk
     * @return true if the chunk was successfully claimed, false if already owned or limit reached
     */
    public static boolean claimChunk(ServerLevel level, ChunkPos chunkPos, Civilization civ) {
        TerritorySavedData data = get(level);

        // Check if chunk is already claimed
        if (data.getCivilizationForChunk(chunkPos).isPresent()) {
            return false;
        }

        // Check civilization's chunk limit
        if (civ.getTerritory().size() >= civ.getMaxChunks()) {
            return false;
        }

        // Claim the chunk
        data.setChunkOwner(chunkPos, civ);
        civ.addChunk(chunkPos);
        data.setDirty();

        return true;
    }

    /**
     * Unclaim a chunk from a civilization.
     *
     * @param level The server level
     * @param chunkPos The chunk to unclaim
     * @param civ The civilization unclaiming the chunk
     */
    public static void unclaimChunk(ServerLevel level, ChunkPos chunkPos, Civilization civ) {
        TerritorySavedData data = get(level);

        data.removeChunkOwner(chunkPos);
        civ.removeChunk(chunkPos);
        data.setDirty();
    }

    /**
     * Get all chunks adjacent to a given chunk position.
     * Useful for checking contiguous territory requirements.
     *
     * @param pos The center chunk position
     * @return Array of 4 adjacent chunk positions (N, S, E, W)
     */
    public static ChunkPos[] getAdjacentChunks(ChunkPos pos) {
        return new ChunkPos[] {
                new ChunkPos(pos.x, pos.z - 1), // North
                new ChunkPos(pos.x, pos.z + 1), // South
                new ChunkPos(pos.x + 1, pos.z), // East
                new ChunkPos(pos.x - 1, pos.z)  // West
        };
    }

    /**
     * Check if a chunk is adjacent to any of a civilization's territory.
     *
     * @param chunkPos The chunk to check
     * @param civ The civilization
     * @return true if the chunk borders the civilization's territory
     */
    public static boolean isAdjacentToTerritory(ChunkPos chunkPos, Civilization civ) {
        if (civ.getTerritory().isEmpty()) {
            return true; // First chunk is always allowed
        }

        for (ChunkPos adjacent : getAdjacentChunks(chunkPos)) {
            if (civ.getTerritory().contains(adjacent)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculate the total area of a civilization's territory in blocks.
     *
     * @param civ The civilization
     * @return Total area in square blocks (each chunk = 256 blocks)
     */
    public static int getTerritoryArea(Civilization civ) {
        return civ.getTerritory().size() * 256; // 16x16 blocks per chunk
    }

    /**
     * Check if an entity is within any civilization's territory.
     *
     * @param level The server level
     * @param entity The entity to check
     * @return Optional containing the civilization if entity is in claimed territory
     */
    public static Optional<Civilization> getCivilizationAt(ServerLevel level, Entity entity) {
        ChunkPos chunkPos = new ChunkPos(entity.blockPosition());
        return getCivilizationForChunk(level, chunkPos);
    }

    /**
     * Get the monument position for a civilization.
     * The monument is stored in the civilization's data.
     *
     * @param civ The civilization
     * @return The monument's BlockPos, or null if not set
     */
    public static BlockPos getMonumentPosition(Civilization civ) {
        return civ.getMonumentPos();
    }

    /**
     * Check if a position is within range of a civilization's monument.
     * Used for claiming chunks (must be within certain distance of monument).
     *
     * @param pos The position to check
     * @param civ The civilization
     * @param maxDistance Maximum distance in chunks
     * @return true if position is within range
     */
    public static boolean isWithinMonumentRange(BlockPos pos, Civilization civ, int maxDistance) {
        BlockPos monumentPos = civ.getMonumentPos();
        if (monumentPos == null) return false;

        ChunkPos posChunk = new ChunkPos(pos);
        ChunkPos monumentChunk = new ChunkPos(monumentPos);

        int chunkDistanceX = Math.abs(posChunk.x - monumentChunk.x);
        int chunkDistanceZ = Math.abs(posChunk.z - monumentChunk.z);

        return Math.max(chunkDistanceX, chunkDistanceZ) <= maxDistance;
    }
}