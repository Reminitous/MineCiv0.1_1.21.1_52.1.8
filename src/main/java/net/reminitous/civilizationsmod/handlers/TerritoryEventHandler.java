package net.reminitous.civilizationsmod.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.data.CivilizationRecord;
import net.reminitous.civilizationsmod.data.TerritorySavedData;

import java.util.Optional;

/**
 * Basic event handler: prevents container access and basic PvP restrictions based on chunk ownership.
 */
@Mod.EventBusSubscriber(modid = CivilizationsMod.MODID)
public class TerritoryEventHandler {

    private static final int MAX_CHUNKS_PER_CIV = 100; // default cap; adjust to your rules

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = player.getCommandSenderWorld() instanceof ServerLevel sl ? sl : null;
        if (level == null) return;

        BlockPos pos = event.getPos();
        ChunkPos cp = new ChunkPos(pos);
        TerritorySavedData data = TerritorySavedData.get(level);
        Optional<CivilizationRecord> ownerOpt = data.getCivForChunk(cp);
        if (!ownerOpt.isPresent()) return;

        CivilizationRecord owner = ownerOpt.get();
        String playerCiv = getPlayerCivilizationId(player);
        if (playerCiv == null || !playerCiv.equals(owner.id)) {
            Block block = level.getBlockState(pos).getBlock();
            if (isContainerBlock(block)) {
                event.setCanceled(true);
                player.displayClientMessage(
                        Component.literal("That storage is owned by " + owner.displayName), true
                );
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer attacker) ||
                !(event.getEntity() instanceof ServerPlayer victim)) return;

        ServerLevel level = victim.getCommandSenderWorld() instanceof ServerLevel sl ? sl : null;
        if (level == null) return;

        ChunkPos cp = new ChunkPos(victim.blockPosition());
        TerritorySavedData data = TerritorySavedData.get(level);
        Optional<CivilizationRecord> ownerOpt = data.getCivForChunk(cp);
        if (!ownerOpt.isPresent()) return;

        CivilizationRecord owner = ownerOpt.get();
        String attackerCiv = getPlayerCivilizationId(attacker);
        String victimCiv = getPlayerCivilizationId(victim);

        boolean victimIsOwner = victimCiv != null && victimCiv.equals(owner.id);
        boolean attackerIsOwner = attackerCiv != null && attackerCiv.equals(owner.id);

        // Prevent attacks in civ-owned territory
        if (victimIsOwner && !attackerIsOwner) {
            event.setCanceled(true);
        }
    }

    // Helper: extract player's civilization id from your CivilizationManager or player capability
    private static String getPlayerCivilizationId(ServerPlayer player) {
        // TODO: Replace with real lookup logic
        return null;
    }

    private static boolean isContainerBlock(Block block) {
        // Basic vanilla container blocks
        if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST ||
                block == Blocks.BARREL || block == Blocks.FURNACE) {
            return true;
        }

        // Check by registry name for shulker boxes or other containers
        ResourceLocation key = Registry.BLOCK.getKey(block);
        return key != null && key.getPath().contains("shulker");
    }

}
