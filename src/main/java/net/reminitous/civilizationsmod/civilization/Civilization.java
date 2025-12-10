package net.reminitous.civilizationsmod.data;

import net.minecraft.nbt.CompoundTag;

public class Civilization {
    private String leader;
    private int population;

    public Civilization(String leader, int population) {
        this.leader = leader;
        this.population = population;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    // Serialization
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Leader", leader);
        nbt.putInt("Population", population);
        return nbt;
    }

    // Deserialization
    public static Civilization deserializeNBT(CompoundTag nbt) {
        return new Civilization(nbt.getString("Leader"), nbt.getInt("Population"));
    }
}
