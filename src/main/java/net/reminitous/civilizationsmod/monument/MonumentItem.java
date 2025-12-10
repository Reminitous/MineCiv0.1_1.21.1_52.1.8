package net.reminitous.civilizationsmod.monument;

import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.registry.ModBlocks; // Corrected import
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class MonumentItem extends Item {

    public MonumentItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide) {
            var player = ctx.getPlayer();
            var pos = ctx.getClickedPos().above();

            if (player == null) return InteractionResult.PASS;

            // Check if player already has a civilization
            if (CivilizationManager.get().playerHasCivilization(player)) {
                player.sendSystemMessage(Component.literal("You are already in a civilization!"));
                return InteractionResult.FAIL;
            }

            // Place the monument block
            BlockState state = ModBlocks.MONUMENT_BLOCK.get().defaultBlockState();
            ctx.getLevel().setBlock(pos, state, 3);

            // Create civilization entry
            CivilizationManager.get().createCivilization(player, pos);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }
}
