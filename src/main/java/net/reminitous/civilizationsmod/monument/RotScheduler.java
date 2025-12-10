package net.reminitous.civilizationsmod.monument;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

import net.reminitous.civilizationsmod.data.TerritorySavedData;
import net.reminitous.civilizationsmod.data.CivilizationRecord;

import java.util.ArrayList;
import java.util.List;

public class RotScheduler {

    private static final long WEEK_MS = 7L * 24 * 3600 * 1000L;
    private long accumulator = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        accumulator++;
        if (accumulator < 1200) return; // ~60s at 20 TPS
        accumulator = 0;

        MinecraftServer server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        long now = System.currentTimeMillis();

        for (ServerLevel level : server.getAllLevels()) {

            TerritorySavedData data = TerritorySavedData.get(level);
            if (data == null) continue;

            List<String> expiredCivs = new ArrayList<>();

            // Collect expired civilizations
            for (CivilizationRecord rec : data.getAllRecords()) { // assuming getAllRecords() exists
                if (now - rec.getLastActiveMillis() >= WEEK_MS) {  // use getter
                    expiredCivs.add(rec.getId());                  // use getter
                }
            }

            // Remove each expired civ
            for (String id : expiredCivs) {

                CivilizationRecord rec = data.getRecordById(id);   // use proper getter
                if (rec == null) continue;

                // remove monument block
                if (rec.getMonumentPos() != null) {              // use getter
                    level.setBlock(rec.getMonumentPos(), Blocks.AIR.defaultBlockState(), 3);
                }

                // remove civilization
                data.removeCivilization(id);                     // use proper method
            }

            data.setDirty();                                     // mark data dirty
        }
    }
}
