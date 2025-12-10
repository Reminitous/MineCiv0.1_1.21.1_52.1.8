@Mod.EventBusSubscriber(modid = YourMod.MODID)
public class TerritoryEventHandler {
    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;
        ServerLevel level = (ServerLevel) event.getLevel();
        ChunkPos pos = new ChunkPos(event.getPos());
        TerritorySavedData data = TerritoryManager.get(level);
        Optional<CivilizationRecord> opt = data.getCivForChunk(pos);
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        if (opt.isPresent()) {
            CivilizationRecord owner = opt.get();
            if (!playerBelongsToCiv(player, owner) && !isAlly(player, owner)) {
                // prevent opening containers etc.
                if (isContainerBlock(event.getLevel().getBlockState(event.getPos()).getBlock())) {
                    event.setCanceled(true);
                    player.displayClientMessage(Component.literal("You cannot access that chest; it's owned by " + owner.displayName), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent e) {
        if (e.getSource().getEntity() instanceof ServerPlayer attacker &&
                e.getEntity() instanceof ServerPlayer victim) {
            ServerLevel level = (ServerLevel) victim.level();
            ChunkPos pos = new ChunkPos(victim.blockPosition());
            TerritorySavedData data = TerritoryManager.get(level);
            Optional<CivilizationRecord> opt = data.getCivForChunk(pos);
            if (opt.isPresent()) {
                CivilizationRecord owner = opt.get();
                // if victim is inside someone else's territory, attacker may be allowed to attack,
                // but victim cannot attack owners. Implement your rule: "When a player enters a territory owned by a different civilization, the members of that civilization may attack the player but the player cannot attack the members that own the land."
                // So if attacker is not owner and victim is owner (attacker attacking owner in their own land) -> cancel
                if (isMemberOfCiv(victim, owner) && !isMemberOfCiv(attacker, owner)) {
                    // attacker trying to damage member on their own land -> cancel
                    e.setCanceled(true);
                }
            }
        }
    }
}
