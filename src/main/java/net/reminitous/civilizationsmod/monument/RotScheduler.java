package net.reminitous.civilizationsmod.monument;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

import net.reminitous.civilizationsmod.territory.TerritoryManager;
import net.reminitous.civilizationsmod.territory.TerritorySavedData;
import net.reminitous.civilizationsmod.civilization.CivilizationRecord;

import java.util.ArrayList;
import java.util.List;

public class RotScheduler {

    private static final long WEEK_MS = 7L * 24 * 3600 * 1000L;
    private long accumulator = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        accumulator++;

        // once per 1200 ticks (60s)
        if (accumulator < 1200) return;
        accumulator = 0;

        MinecraftServer server = event.getServer();
        if (server == null) return;

        long now = System.currentTimeMillis();

        // For every dimension:
        for (ServerLevel level : server.getAllLevels()) {

            TerritorySavedData data = TerritoryManager.get(level);
            if (data == null) continue;

            List<String> expiredCivs = new ArrayList<>();

            // Collect expired civilizations
            for (CivilizationRecord rec : data.getAll()) {
                if (now - rec.lastActiveMillis >= WEEK_MS) {
                    expiredCivs.add(rec.id);
                }
            }

            // Remove each expired civ
            for (String id : expiredCivs) {

                CivilizationRecord rec = data.getById(id);

                // remove monument
                if (rec != null && rec.monumentPos != null) {
                    level.setBlock(rec.monumentPos, Blocks.AIR.defaultBlockState(), 3);
                }

                // remove civilization
                data.removeCiv(id);
            }

            data.setDirty();
        }
    }
}
