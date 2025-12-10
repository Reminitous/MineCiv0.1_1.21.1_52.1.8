package net.reminitous.civilizationsmod.client;

import net.reminitous.civilizationsmod.civilization.Civilization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientCivilizationManager {

    private static final Map<UUID, Civilization> CIVILIZATIONS = new HashMap<>();

    /** Update or add a civilization from server data */
    public static void updateCivilization(Civilization civ) {
        CIVILIZATIONS.put(civ.getId(), civ); // note the lowercase 'getId()'
    }

    /** Get civilization by UUID */
    public static Civilization getCivilization(UUID id) {
        return CIVILIZATIONS.get(id);
    }
}
