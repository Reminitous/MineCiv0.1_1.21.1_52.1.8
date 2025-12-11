package net.reminitous.civilizationsmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.registry.ModBlocks;
import net.reminitous.civilizationsmod.registry.ModSounds;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

/**
 * Monument item used to create a civilization.
 * Right-click on the ground to place a monument and start your civilization.
 */
public class MonumentItem extends Item {

    public MonumentItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();

        // Only work on server side
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Player player = ctx.getPlayer();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos clickedPos = ctx.getClickedPos();
        BlockPos monumentPos = clickedPos.above(); // Place above clicked block

        // ===========================
        // VALIDATION CHECKS
        // ===========================

        // Check if player already has a civilization
        if (CivilizationManager.get().playerHasCivilization(player)) {
            serverPlayer.sendSystemMessage(
                    Component.literal("§cYou are already in a civilization!")
            );
            return InteractionResult.FAIL;
        }

        // Check if chunk is already claimed
        ChunkPos chunkPos = new ChunkPos(monumentPos);
        if (TerritoryManager.get(serverLevel).isChunkClaimed(chunkPos)) {
            serverPlayer.sendSystemMessage(
                    Component.literal("§cThis chunk is already claimed by another civilization!")
            );
            return InteractionResult.FAIL;
        }

        // Check if there's space to place the monument
        if (!level.getBlockState(monumentPos).canBeReplaced()) {
            serverPlayer.sendSystemMessage(
                    Component.literal("§cNot enough space to place the monument!")
            );
            return InteractionResult.FAIL;
        }

        // ===========================
        // PLACE MONUMENT & CREATE CIVILIZATION
        // ===========================

        // Place the monument block
        BlockState monumentState = ModBlocks.MONUMENT.get().defaultBlockState();
        level.setBlock(monumentPos, monumentState, 3);

        // Create the civilization
        Civilization civ = CivilizationManager.get().createCivilization(player, monumentPos);

        // Claim the initial chunk
        TerritoryManager.claimChunk(serverLevel, chunkPos, civ);

        // Play sound
        level.playSound(
                null,
                monumentPos,
                ModSounds.MONUMENT_PLACED.get(),
                SoundSource.BLOCKS,
                1.0f,
                1.0f
        );

        // Consume the item
        ctx.getItemInHand().shrink(1);

        // Send success message
        serverPlayer.sendSystemMessage(
                Component.literal("§a§l✓ Civilization Created!")
                        .append(Component.literal("\n§7Name: §f" + civ.getName()))
                        .append(Component.literal("\n§7Leader: §f" + player.getName().getString()))
                        .append(Component.literal("\n§7Starting chunks: §f1"))
        );

        // Save data
        TerritoryManager.get(serverLevel).setDirty();

        return InteractionResult.CONSUME;
    }
}