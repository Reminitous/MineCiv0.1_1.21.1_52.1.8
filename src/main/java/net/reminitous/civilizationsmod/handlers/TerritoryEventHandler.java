package net.reminitous.civilizationsmod.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.data.CivilizationRecord;
import net.reminitous.civilizationsmod.data.TerritorySavedData;

import java.util.Optional;

/**
 * Basic event handler: prevents container access and basic PvP restrictions based on chunk ownership.
 */
@Mod.EventBusSubscriber(modid = net.reminitous.civilizationsmod.CivilizationsMod.MODID)
public class TerritoryEventHandler {

    private static final int MAX_CHUNKS_PER_CIV = 100; // default cap; adjust to your rules

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        ChunkPos cp = new ChunkPos(pos);
        TerritorySavedData data = TerritorySavedData.get(level);
        Optional<CivilizationRecord> ownerOpt = data.getCivForChunk(cp);
        if (!ownerOpt.isPresent()) return;
        CivilizationRecord owner = ownerOpt.get();
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        // simple check: player cannot open containers if they are not in same civ
        String playerCiv = getPlayerCivilizationId(player);
        if (playerCiv == null || !playerCiv.equals(owner.id)) {
            // We prevent opening containers by canceling right click on container blocks.
            // We proactively cancel for any right-click on the block if it is a container.
            if (isContainerBlock(level.getBlockState(pos).getBlock())) {
                event.setCanceled(true);
                player.displayClientMessage(Component.literal("That storage is owned by " + owner.displayName), true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer attacker &&
                event.getEntity() instanceof ServerPlayer victim) {
            if (!(victim.level instanceof ServerLevel level)) return;
            ChunkPos cp = new ChunkPos(victim.blockPosition());
            TerritorySavedData data = TerritorySavedData.get(level);
            Optional<CivilizationRecord> ownerOpt = data.getCivForChunk(cp);
            if (!ownerOpt.isPresent()) return;
            CivilizationRecord owner = ownerOpt.get();
            String attackerCiv = getPlayerCivilizationId(attacker);
            String victimCiv = getPlayerCivilizationId(victim);
            // rule: when a player enters a territory owned by a different civ, members of that civ may attack the player,
            // but the player cannot attack the members that own the land.
            boolean victimIsOwner = victimCiv != null && victimCiv.equals(owner.id);
            boolean attackerIsOwner = attackerCiv != null && attackerCiv.equals(owner.id);
            if (victimIsOwner && !attackerIsOwner) {
                // attacker trying to attack a resident in their own civ territory -> cancel
                event.setCanceled(true);
            }
            // other cases (owner attacking intruder) are allowed per your rule
        }
    }

    // Helper: extract player's civilization id from wherever you store it (example: scoreboard, capability, or a manager).
    private static String getPlayerCivilizationId(ServerPlayer player) {
        // Placeholder: adapt to your membership logic.
        // If you store player->civ mapping in a manager class, query that instead.
        // For now, return null (no civ).
        return null;
    }

    private static boolean isContainerBlock(net.minecraft.world.level.block.Block block) {
        // Basic checks for common container blocks: Chest, ShulkerBox, Barrel, Furnace
        // Safer to use instance checks against Block classes in your real integration.
        String name = block.getRegistryName() != null ? block.getRegistryName().getPath() : block.toString();
        return name.contains("chest") || name.contains("shulker_box") || name.contains("barrel") || name.contains("furnace") || name.contains("hopper");
    }
}
