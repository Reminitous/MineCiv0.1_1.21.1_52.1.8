package net.reminitous.civilizationsmod.civilization;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.reminitous.civilizationsmod.civilization.Civilization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CivilizationManager {

    private static final Map<UUID, Civilization> CIVILIZATIONS = new HashMap<>();

    /** Load all civilizations from saved data */
    public static void loadData(MinecraftServer server) {
        // TODO: Replace with actual saved data loading logic
        // Example: populate CIVILIZATIONS map from TerritorySavedData or other storage
        CIVILIZATIONS.clear();
    }

    /** Update civilizations each tick */
    public static void tick() {
        for (Civilization civ : CIVILIZATIONS.values()) {
            civ.tick();
        }
    }

    /** Get a player's civilization by their UUID */
    public static Civilization getCivilizationForPlayer(UUID playerUUID) {
        return CIVILIZATIONS.get(playerUUID);
    }

    /** Sync a civilization to a specific player */
    public static void syncToPlayer(ServerPlayer player) {
        Civilization civ = getCivilizationForPlayer(player.getUUID());
        if (civ != null) {
            // TODO: send civ data to client via network packet
        }
    }

    /** Add or update a civilization in memory */
    public static void addOrUpdateCivilization(Civilization civ) {
        CIVILIZATIONS.put(civ.getId(), civ);
    }
}
