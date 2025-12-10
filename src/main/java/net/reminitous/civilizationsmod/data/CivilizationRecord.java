package net.reminitous.civilizationsmod.data;

public class CivilizationRecord {

    // Unique ID for this civilization
    public final String id;

    // Display name of the civilization
    public final String displayName;

    public CivilizationRecord(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    // Optionally, add other fields like leader, color, etc.
}
