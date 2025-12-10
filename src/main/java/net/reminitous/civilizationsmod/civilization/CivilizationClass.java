package net.reminitous.civilizationsmod.Civilization;

public enum CivilizationClass {
    AGRICULTURAL("Agricultural"),
    WARLIKE("Warlike"),
    TECHNOLOGY("Technology"),
    MYSTIC("Mystic"),
    MERCHANT("Merchant");

    private final String displayName;

    CivilizationClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
