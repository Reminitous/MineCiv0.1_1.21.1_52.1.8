package net.reminitous.civilizationsmod.territory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.reminitous.civilizationsmod.civilization.CivilizationRecord;

public class ChunkClaimUtil {

    public static final int MAX_CHUNKS = 64; // or load from config

    public static boolean canPlayerClaimChunk(ServerPlayer player, ChunkPos pos, TerritorySavedData data) {

        // Already claimed
        if (data.getCivForChunk(pos).isPresent()) return false;

        // Player's civilization
        CivilizationRecord rec = data.getById(getPlayerCivId(player));
        if (rec == null) return false;

        // Limit
        return rec.claimedChunks.size() < MAX_CHUNKS;
    }

    private static String getPlayerCivId(ServerPlayer player) {
        // Hook into your actual civ membership system
        return player.getUUID().toString();
    }
}
