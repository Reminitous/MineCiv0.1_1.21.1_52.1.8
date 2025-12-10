package net.reminitous.civilizationsmod.civilization;

import java.util.UUID;

public class Civilization {

    private final UUID id;
    private final String name;

    public Civilization(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /** Getter for the civilization ID */
    public UUID getId() {
        return id;
    }

    /** Getter for the civilization name */
    public String getName() {
        return name;
    }

    /** Tick method if needed for updates each server tick */
    public void tick() {
        // Implement periodic logic for civilizations if needed
    }
}
