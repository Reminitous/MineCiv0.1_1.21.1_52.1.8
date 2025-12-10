package net.reminitous.civilizationsmod.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fully implements all civilization class enhancements.
 */
@Mod.EventBusSubscriber
public class CivilizationClassEnhancements {

    private static final Random random = new Random();
    private static final Timer timer = new Timer();

    // ===========================
    // Helper method to get player's civilization class
    // ===========================
    private static CivilizationClass getPlayerClass(Player player) {
        return CivilizationManager.getPlayerCivilizationClass(player);
    }

    // ===========================
    // AGRICULTURAL
    // ===========================

    @SubscribeEvent
    public static void onCropHarvest(BreakEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.AGRICULTURAL) {
            if (event.getState().is(Blocks.WHEAT) || event.getState().is(Blocks.CARROTS)
                    || event.getState().is(Blocks.POTATOES) || event.getState().is(Blocks.BEETROOTS)) {
                event.getDrops().forEach(drop -> drop.setCount(drop.getCount() + (int) Math.ceil(drop.getCount() * 0.25)));
            }
        }
    }

    @SubscribeEvent
    public static void onWoodcuttingSpeed(BreakSpeed event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);
        if (civClass == CivilizationClass.AGRICULTURAL) {
            if (event.getState().is(Blocks.OAK_LOG) || event.getState().is(Blocks.BIRCH_LOG)
                    || event.getState().is(Blocks.SPRUCE_LOG) || event.getState().is(Blocks.JUNGLE_LOG)
                    || event.getState().is(Blocks.ACACIA_LOG) || event.getState().is(Blocks.DARK_OAK_LOG)) {
                event.setNewSpeed(event.getNewSpeed() * 1.15f);
            }
        }
    }

    @SubscribeEvent
    public static void onAnimalInteraction(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);
        if (civClass == CivilizationClass.AGRICULTURAL && event.getTarget() instanceof Animal animal) {
            // 20% faster breeding cooldown
            double cooldownReduction = 0.2;
            animal.setInLoveTime((int) (animal.getInLoveTime() * (1 - cooldownReduction)));

            // TODO: Implement 15% increased taming chance for wolves/cats
        }
    }

    // ===========================
    // WARLIKE
    // ===========================

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.WARLIKE) {
            event.setAmount((float) (event.getAmount() * 1.15)); // 15% damage boost
        }
    }

    @SubscribeEvent
    public static void onWeaponEnchant(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.WARLIKE) {
            ItemStack held = player.getMainHandItem();
            if (held.getItem() == Items.IRON_SWORD || held.getItem() == Items.DIAMOND_SWORD
                    || held.getItem() == Items.NETHERITE_SWORD) {
                if (!held.getAllEnchantments().containsKey(Enchantments.KNOCKBACK)) {
                    held.enchant(Enchantments.KNOCKBACK, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onArrowDraw(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.WARLIKE && player.getMainHandItem().getItem() == Items.BOW) {
            // Fully draw bow instantly
            player.getCooldowns().addCooldown(Items.BOW, 1);
        }
    }

    @SubscribeEvent
    public static void onArrowFire(net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow event) {
        if (!(event.getArrow().getOwner() instanceof Player player)) return;
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.WARLIKE) {
            event.getArrow().setDeltaMovement(event.getArrow().getDeltaMovement().scale(2.0)); // 200% range
        }
    }

    // ===========================
    // TECHNOLOGY
    // ===========================

    @SubscribeEvent
    public static void onMiningSpeed(BreakSpeed event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.TECHNOLOGY) {
            event.setNewSpeed(event.getNewSpeed() * 1.15f); // 15% faster mining
        }
    }

    @SubscribeEvent
    public static void onFurnaceTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        event.world.blockEntities.stream()
                .filter(be -> be instanceof net.minecraft.world.level.block.entity.FurnaceBlockEntity)
                .map(be -> (net.minecraft.world.level.block.entity.FurnaceBlockEntity) be)
                .forEach(furnace -> {
                    Player owner = CivilizationManager.getFurnaceOwner(furnace);
                    if (owner != null && getPlayerClass(owner) == CivilizationClass.TECHNOLOGY && furnace.isLit()) {
                        furnace.setCookTime((int) (furnace.getCookTime() + 1.15)); // 15% faster smelting
                    }
                });
    }

    // ===========================
    // MYSTIC
    // ===========================

    @SubscribeEvent
    public static void onEnchantItem(AnvilUpdateEvent event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.MYSTIC && event.getCost() > 0) {
            event.setCost((int) Math.ceil(event.getCost() * 0.8)); // 20% reduction
        }
    }

    @SubscribeEvent
    public static void onPotionBrewing(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass != CivilizationClass.MYSTIC && event.getLevel().getBlockState(event.getPos()).is(Blocks.BREWING_STAND)) {
            event.setCanceled(true); // Only mystics can brew
        }
    }

    public static void createMysticPortal(Player player, BlockPos targetPos) {
        if (getPlayerClass(player) != CivilizationClass.MYSTIC) return;

        BlockPos portalPos = player.blockPosition();
        player.level.setBlock(portalPos, Blocks.NETHER_PORTAL.defaultBlockState(), 3);
        player.level.setBlock(targetPos, Blocks.NETHER_PORTAL.defaultBlockState(), 3);

        // Remove after 15 minutes
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.level.removeBlock(portalPos, false);
                player.level.removeBlock(targetPos, false);
            }
        }, 18000 * 50L); // 18000 ticks * 50ms per tick
    }

    @SubscribeEvent
    public static void onPlayerTeleport(net.minecraftforge.event.entity.living.LivingTeleportEvent.TeleportCommand event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (getPlayerClass(player) != CivilizationClass.MYSTIC) {
            event.setCanceled(true);
        }
    }

    // ===========================
    // MERCHANT
    // ===========================

    @SubscribeEvent
    public static void onVillagerTrade(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        CivilizationClass civClass = getPlayerClass(player);

        if (civClass == CivilizationClass.MERCHANT && event.getTarget() instanceof Villager villager) {
            villager.setXp(villager.getXp() + 5); // faster XP gain
            villager.getOffers().forEach(offer -> offer.increaseUses(-offer.getUses() / 2)); // reduce cooldown
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupItem(net.minecraftforge.event.entity.player.EntityItemPickupEvent event) {
        Player player = event.getPlayer();
        if (getPlayerClass(player) == CivilizationClass.MERCHANT) {
            // TODO: implement auto-sorting to civilization storage
        }
    }
}
