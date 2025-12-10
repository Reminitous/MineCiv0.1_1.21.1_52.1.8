package net.reminitous.civilizationsmod.Civilization;

import net.minecraft.world.level.ChunkPos;
import java.util.*;

public class CivilizationManager {

    private static final Map<UUID, CivilizationData> civilizations = new HashMap<>();
    private static final Map<UUID, UUID> playerToCiv = new HashMap<>(); // player -> civ leader mapping

    public static CivilizationData createCivilization(String name, UUID leaderId, CivilizationClass civClass, ChunkPos startingChunk) {
        CivilizationData civ = new CivilizationData(name, leaderId, civClass);
        civ.addChunk(startingChunk);
        civilizations.put(leaderId, civ);
        playerToCiv.put(leaderId, leaderId);
        return civ;
    }

    public static boolean addMember(UUID leaderId, UUID playerId) {
        CivilizationData civ = civilizations.get(leaderId);
        if (civ == null) return false;
        if (playerToCiv.containsKey(playerId)) return false; // player already in a civ
        boolean added = civ.addMember(playerId);
        if (added) playerToCiv.put(playerId, leaderId);
        return added;
    }

    public static boolean removeMember(UUID leaderId, UUID playerId) {
        CivilizationData civ = civilizations.get(leaderId);
        if (civ == null) return false;
        boolean removed = civ.removeMember(playerId);
        if (removed) playerToCiv.remove(playerId);
        return removed;
    }

    public static CivilizationData getCivilization(UUID playerId) {
        UUID leaderId = playerToCiv.get(playerId);
        if (leaderId == null) return null;
        return civilizations.get(leaderId);
    }

    public static void claimChunk(UUID playerId, ChunkPos chunk) {
        CivilizationData civ = getCivilization(playerId);
        if (civ != null) {
            civ.addChunk(chunk);
        }
    }

    public static void removeCivilization(UUID leaderId) {
        CivilizationData civ = civilizations.get(leaderId);
        if (civ != null) {
            for (UUID member : civ.getMembers()) {
                playerToCiv.remove(member);
            }
            civilizations.remove(leaderId);
        }
    }

    public static Collection<CivilizationData> getAllCivilizations() {
        return civilizations.values();
    }

    public static boolean isChunkClaimed(ChunkPos chunk) {
        return civilizations.values().stream().anyMatch(civ -> civ.getTerritory().contains(chunk));
    }

    public static void updateActivity(UUID playerId) {
        CivilizationData civ = getCivilization(playerId);
        if (civ != null) {
            civ.updateLastActive();
        }
    }
}
