package net.reminitous.civilizationsmod.npc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.civilization.CivilizationClass;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.Random;

public class NPCSpawner {

    private static final Random random = new Random();

    // Call this method every few ticks or periodically
    public static void spawnNPCs(ServerLevel level) {
        for (Civilization civ : CivilizationManager.getAllCivilizations()) {
            // Calculate target NPC count based on civilization level
            int targetNPCCount = civ.getVirtualLevel() + 2; // base + level scaling

            // Count current NPCs inside civilization territory
            int currentNPCCount = countNPCsInTerritory(level, civ);

            // Spawn missing NPCs
            int toSpawn = targetNPCCount - currentNPCCount;
            for (int i = 0; i < toSpawn; i++) {
                spawnNPCForCivilization(level, civ);
            }
        }
    }

    private static int countNPCsInTerritory(ServerLevel level, Civilization civ) {
        int count = 0;
        for (Monster monster : level.getEntitiesOfClass(Monster.class, civ.getTerritoryBoundingBox())) {
            if (CivilizationManager.getCivilization(monster) == civ) count++;
        }
        for (Villager villager : level.getEntitiesOfClass(Villager.class, civ.getTerritoryBoundingBox())) {
            if (CivilizationManager.getCivilization(villager) == civ) count++;
        }
        return count;
    }

    private static void spawnNPCForCivilization(ServerLevel level, Civilization civ) {
        EntityType<?> npcType;

        // Randomly pick NPC based on class
        switch (civ.getCivilizationClass()) {
            case AGRICULTURE:
                npcType = random.nextBoolean() ? FarmerNPC::new : ShepherdNPC::new;
                break;
            case WARLIKE:
                npcType = random.nextInt(2) == 0 ? WarriorNPC::new : KnightNPC::new;
                break;
            case TECHNOLOGY:
                npcType = EngineerNPC::new;
                break;
            case MYSTIC:
                npcType = WizardNPC::new;
                break;
            case MERCHANT:
                npcType = MerchantNPC::new;
                break;
            default:
                npcType = FarmerNPC::new; // fallback
        }

        // Pick random position inside civilization territory
        BlockPos spawnPos = TerritoryManager.getRandomPositionInTerritory(level, civ);

        if (spawnPos != null) {
            if (Villager.class.isAssignableFrom(npcType.getBaseClass())) {
                Villager npc = (Villager) npcType.create(level);
                npc.moveTo(spawnPos, 0.0f, 0.0f);
                level.addFreshEntity(npc);
            } else if (Monster.class.isAssignableFrom(npcType.getBaseClass())) {
                Monster npc = (Monster) npcType.create(level);
                npc.moveTo(spawnPos, 0.0f, 0.0f);
                level.addFreshEntity(npc);
            }
        }
    }
}
