package net.reminitous.civilizationsmod.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class CivilizationData extends SavedData {
    private final Map<String, Civilization> civilizations = new HashMap<>();

    public CivilizationData() {
        super();
    }

    // Add a new civilization
    public void addCivilization(String name, Civilization civilization) {
        civilizations.put(name, civilization);
        setDirty(); // Mark data as modified
    }

    // Get a civilization by name
    public Civilization getCivilization(String name) {
        return civilizations.get(name);
    }

    // Remove a civilization
    public void removeCivilization(String name) {
        civilizations.remove(name);
        setDirty();
    }

    // Serialization to NBT
    @Override
    public CompoundTag save(CompoundTag nbt) {
        CompoundTag civsTag = new CompoundTag();
        for (Map.Entry<String, Civilization> entry : civilizations.entrySet()) {
            civsTag.put(entry.getKey(), entry.getValue().serializeNBT());
        }
        nbt.put("civilizations", civsTag);
        return nbt;
    }

    // Deserialization from NBT
    public static CivilizationData load(CompoundTag nbt) {
        CivilizationData data = new CivilizationData();
        CompoundTag civsTag = nbt.getCompound("civilizations");
        for (String key : civsTag.getAllKeys()) {
            data.civilizations.put(key, Civilization.deserializeNBT(civsTag.getCompound(key)));
        }
        return data;
    }
}
