package net.reminitous.civilizationsmod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;

@Mod.EventBusSubscriber(modid = "your_mod_id")
public class CivilizationServerEvents {

    /** Load civilization data when server starts */
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CivilizationManager.loadData(event.getServer());
    }

    /** Update civilizations every server tick */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return; // run only on END phase
        CivilizationManager.tick();
    }

    /** Sync civilization data to a player when they join */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity(); // correct access
        CivilizationManager.syncToPlayer(player);
    }

}
