package net.reminitous.civilizationsmod.territory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.reminitous.civilizationsmod.data.CivilizationRecord;
import net.reminitous.civilizationsmod.data.TerritorySavedData;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;

public class ChunkClaimUtil {

    public static final int MAX_CHUNKS = 64;

    public static boolean canPlayerClaimChunk(ServerPlayer player, ChunkPos pos, TerritorySavedData data) {
        // Already claimed
        if (data.isChunkClaimed(pos)) return false;

        // Player's civilization
        Civilization civ = CivilizationManager.getCivilizationForPlayer(player);
        if (civ == null) return false;

        // Limit
        return civ.getTerritory().size() < MAX_CHUNKS;
    }
}
