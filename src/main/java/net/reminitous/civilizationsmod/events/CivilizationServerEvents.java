package net.reminitous.civilizationsmod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.npc.BaseNPC;
import net.reminitous.civilizationsmod.npc.*;

import java.time.Instant;

/**
 * Handles server-side civilization events:
 * virtual levels, NPC actions, monument decay, and war management.
 */
@Mod.EventBusSubscriber
public class CivilizationServerEvents {

    private static final CivilizationManager civManager = new CivilizationManager();
    private static long lastTickTime = Instant.now().getEpochSecond();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        long currentTime = Instant.now().getEpochSecond();
        long deltaTime = currentTime - lastTickTime;

        // Run updates every 60 seconds
        if (deltaTime < 60) return;
        lastTickTime = currentTime;

        for (Civilization civ : civManager.getAllCivilizations()) {
            updateVirtualLevel(civ);
            checkMonumentDecay(civ, currentTime);
            handleNPCs(civ);
            handleWars(civ);
        }
    }

    private static void updateVirtualLevel(Civilization civ) {
        // TODO: Implement experience system (quests, resources, member activity)
        int newLevel = civ.getVirtualLevel() + 1; // Placeholder
        civ.setVirtualLevel(newLevel);
    }

    /**
     * Checks if the civilization's monument should decay due to inactivity.
     */
    private static void checkMonumentDecay(Civilization civ, long currentTime) {
        boolean allOffline = true;
        long lastOnline = civ.getLastActiveTimestamp(); // Track last member online

        for (String member : civ.getMembers()) {
            ServerPlayer player = findPlayerByName(member);
            if (player != null && player.isOnline()) {
                allOffline = false;
                lastOnline = currentTime;
                break;
            }
        }

        civ.setLastActiveTimestamp(lastOnline);

        long oneWeekSeconds = 7 * 24 * 60 * 60;
        if (allOffline && (currentTime - lastOnline) >= oneWeekSeconds) {
            // Remove monument block from the world
            BlockPos monumentPos = civ.getMonumentPos();
            Level level = civ.getMonumentLevel();
            if (level != null && monumentPos != null) {
                level.removeBlock(monumentPos, false);
            }

            // Free claimed chunks
            civManager.freeChunks(civ.getTerritoryChunks());

            // Stop all NPCs
            for (BaseNPC npc : civ.getNPCs()) {
                npc.setAlive(false);
            }

            // Mark civilization as inactive
            civ.setActive(false);

            // Notify all online players
            for (ServerPlayer player : Level.getServer().getPlayerList().getPlayers()) {
                player.sendSystemMessage(
                        "Civilization '" + civ.getName() + "' has decayed due to inactivity."
                );
            }
        }
    }

    private static void handleNPCs(Civilization civ) {
        if (!civ.isActive()) return; // Skip NPCs if civilization is inactive

        for (BaseNPC npc : civ.getNPCs()) {
            if (!npc.isAlive()) continue;

            switch (npc.getNPCType()) {
                case FARMER:
                    ((FarmerNPC) npc).tick();
                    break;
                case SHEPHERD:
                    ((ShepherdNPC) npc).tick();
                    break;
                case LUMBERJACK:
                    ((LumberjackNPC) npc).tick();
                    break;
                case KNIGHT:
                    ((KnightNPC) npc).tick();
                    break;
                case WARRIOR:
                    ((WarriorNPC) npc).tick();
                    break;
                case ENGINEER:
                    ((EngineerNPC) npc).tick();
                    break;
                case WIZARD:
                    ((WizardNPC) npc).tick();
                    break;
                case MERCHANT:
                    ((MerchantNPC) npc).tick();
                    break;
                default:
                    break;
            }
        }
    }

    private static void handleWars(Civilization civ) {
        CivilizationWarState war = civ.getCurrentWar();
        if (war != null) {
            if (war.isWarOver()) {
                civManager.endWar(war);
            } else {
                // TODO: Apply war mechanics
                // e.g., decrease civilization health on member deaths or block destruction
            }
        }
    }

    private static ServerPlayer findPlayerByName(String playerName) {
        if (Level.getServer() == null) return null;
        for (ServerPlayer player : Level.getServer().getPlayerList().getPlayers()) {
            if (player.getName().getString().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    public static CivilizationManager getCivManager() {
        return civManager;
    }
}
