package net.reminitous.civilizationsmod.monument;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.reminitous.civilizationsmod.Civilization.CivilizationClass;
import net.reminitous.civilizationsmod.Civilization.CivilizationManager;
import net.minecraft.world.level.ChunkPos;
import java.util.UUID;

public class MonumentBlock extends Block {

    public MonumentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        UUID playerId = player.getUUID();
        ChunkPos chunk = new ChunkPos(pos);

        if (CivilizationManager.getCivilization(playerId) == null) {
            // First-time placement
            CivilizationClass civClass = CivilizationClass.AGRICULTURAL; // TODO: Prompt player to select class via GUI
            String civName = player.getName().getString() + "'s Civ"; // TODO: Prompt for name
            CivilizationManager.createCivilization(civName, playerId, civClass, chunk);
            player.sendMessage(net.minecraft.network.chat.Component.literal("Civilization '" + civName + "' created!"), player.getUUID());
        } else {
            // Already in a civ -> open monument GUI
            // TODO: implement GUI handling here
        }

        return InteractionResult.CONSUME;
    }
}
