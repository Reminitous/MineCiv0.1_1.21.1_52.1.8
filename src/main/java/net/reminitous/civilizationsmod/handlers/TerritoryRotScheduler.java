package net.reminitous.civilizationsmod.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.data.CivilizationRecord;
import net.reminitous.civilizationsmod.data.TerritorySavedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs periodically (~every 60s) to check for inactive civilizations
 * and remove them if they have been inactive for a week.
 */
@Mod.EventBusSubscriber(modid = CivilizationsMod.MODID)
public class TerritoryRotScheduler {

    private static final long WEEK_MS = 7L * 24 * 60 * 60 * 1000; // 1 week in ms
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter < 1200) return; // ~60s at 20 TPS
        tickCounter = 0;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerLevel level : server.getAllLevels()) {
            TerritorySavedData data = TerritorySavedData.get(level);
            long now = System.currentTimeMillis();
            List<String> toRemove = new ArrayList<>();

            // Find civilizations inactive for a week
            for (CivilizationRecord r : data.getAll()) {
                if (now - r.lastActiveMillis >= WEEK_MS) {
                    toRemove.add(r.id);
                }
            }

            // Remove inactive civilizations
            for (String id : toRemove) {
                CivilizationRecord r = data.getById(id);
                if (r == null) continue;

                // Remove monument block if recorded
                if (r.monumentChunkLong != Long.MIN_VALUE) {
                    ChunkPos cp = new ChunkPos(r.monumentChunkLong);
                    int bx = (cp.getMinBlockX() + cp.getMaxBlockX()) / 2;
                    int bz = (cp.getMinBlockZ() + cp.getMaxBlockZ()) / 2;
                    level.setBlockAndUpdate(new BlockPos(bx, 64, bz), Blocks.AIR.defaultBlockState());
                }

                data.removeCivilization(id);
            }
        }
    }
}
