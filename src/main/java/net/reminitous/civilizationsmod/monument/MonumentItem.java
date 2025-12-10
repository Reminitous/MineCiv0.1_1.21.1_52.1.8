package net.reminitous.civilizationsmod.monument;

import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

public class MonumentItem extends Item {

    public MonumentItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide) {
            var player = ctx.getPlayer();
            var pos = ctx.getClickedPos().above();

            if (CivilizationManager.get().playerHasCivilization(player)) {
                player.sendSystemMessage("You are already in a civilization!");
                return InteractionResult.FAIL;
            }

            // Place block
            ctx.getLevel().setBlock(pos, ModBlocks.MONUMENT_BLOCK.get().defaultBlockState(), 3);

            // Create civilization entry
            CivilizationManager.get().createCivilization(player, pos);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
}
