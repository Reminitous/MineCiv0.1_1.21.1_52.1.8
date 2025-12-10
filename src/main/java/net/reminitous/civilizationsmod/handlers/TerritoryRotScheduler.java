package net.reminitous.civilizationsmod.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.data.CivilizationRecord;
import net.reminitous.civilizationsmod.data.TerritorySavedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs periodically (every ~60s) to check for inactive civilizations and remove them if they've been inactive for a week.
 */
@Mod.EventBusSubscriber(modid = net.reminitous.civilizationsmod.CivilizationsMod.MODID)
public class TerritoryRotScheduler {
    private static final long WEEK_MS = 7L * 24 * 60 * 60 * 1000;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tickCounter++;
        if (tickCounter < 1200) return; // ~60s at 20 TPS
        tickCounter = 0;
        MinecraftServer server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        for (ServerLevel level : server.getAllLevels()) {
            TerritorySavedData data = TerritorySavedData.get(level);
            long now = System.currentTimeMillis();
            List<String> toRemove = new ArrayList<>();
            for (CivilizationRecord r : data.getAll()) {
                if (now - r.lastActiveMillis >= WEEK_MS) {
                    toRemove.add(r.id);
                }
            }
            for (String id : toRemove) {
                CivilizationRecord r = data.getById(id);
                if (r == null) continue;
                // Remove monument block if we recorded monumentChunk
                if (r.monumentChunkLong != Long.MIN_VALUE) {
                    ChunkPos cp = new ChunkPos(r.monumentChunkLong);
                    int bx = (cp.getMinBlockX() + cp.getMaxBlockX()) / 2;
                    int bz = (cp.getMinBlockZ() + cp.getMaxBlockZ()) / 2;
                    level.setBlockAndUpdate(new net.minecraft.core.BlockPos(bx, 64, bz), Blocks.AIR.defaultBlockState());
                }
                data.removeCivilization(id);
            }
        }
    }
}
