package net.reminitous.civilizationsmod.civilization;

import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple in-memory manager of civilizations. This manager holds civ data while
 * the server is running. Persist/load should be done via a SavedData instance
 * (see TerritorySavedData).
 */
public class CivilizationManager {

    // map civId -> CivilizationData
    private static final Map<UUID, CivilizationData> CIVILIZATIONS = new HashMap<>();

    /** Return immutable view of all civs (caller must not mutate) */
    public static Collection<CivilizationData> getAllCivilizations() {
        return CIVILIZATIONS.values();
    }

    public static CivilizationData getCivilizationById(UUID id) {
        return CIVILIZATIONS.get(id);
    }

    /** Find the civilization that a player is a member of (or null) */
    public static CivilizationData getCivilizationForPlayer(UUID playerId) {
        for (CivilizationData civ : CIVILIZATIONS.values()) {
            if (civ.isMember(playerId)) return civ;
        }
        return null;
    }

    /** Convenience for ServerPlayer */
    public static CivilizationData getCivilizationForPlayer(ServerPlayer player) {
        return getCivilizationForPlayer(player.getUUID());
    }

    public static void addOrUpdateCivilization(UUID id, CivilizationData civ) {
        CIVILIZATIONS.put(id, civ);
    }

    public static void removeCivilization(UUID id) {
        CIVILIZATIONS.remove(id);
    }

    public static boolean civNameExists(String name) {
        return CIVILIZATIONS.values().stream()
                .anyMatch(c -> c.getName() != null && c.getName().equalsIgnoreCase(name));
    }

    // TODO: networking: implement syncToPlayer / syncAll using a SimpleChannel
    // TODO: persistence: call TerritorySavedData instance to persist changes to disk
}
