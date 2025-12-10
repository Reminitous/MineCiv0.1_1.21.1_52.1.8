package net.reminitous.civilizationsmod.territory;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.entity.player.Player;

public class TerritoryEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new TerritoryEvents());
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        Player attacker = event.getPlayer();
        if (!(event.getTarget() instanceof Player target)) return;

        String attackerCiv = TerritoryManager.getCivilizationAt(attacker.blockPosition());
        String targetCiv = TerritoryManager.getCivilizationAt(target.blockPosition());

        if (attackerCiv != null && attackerCiv.equals(targetCiv)) {
            event.setCanceled(true); // Cannot attack own civilization
        }
    }
}
