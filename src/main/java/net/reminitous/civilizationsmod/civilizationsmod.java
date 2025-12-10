package net.reminitous.civilizationsmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.reminitous.civilizationsmod.network.PacketHandler;
import net.reminitous.civilizationsmod.territory.TerritoryEvents;
import net.reminitous.civilizationsmod.registry.ModBlocks;
import net.reminitous.civilizationsmod.registry.ModBlockEntities;
import net.reminitous.civilizationsmod.registry.ModItems;

@Mod(CivilizationsMod.MODID)
public class CivilizationsMod {
    public static final String MODID = "mineciv";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CivilizationsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register DeferredRegisters
        ModItems.register(modEventBus);         // Items
        ModBlocks.register(modEventBus);        // Blocks
        ModBlockEntities.register(modEventBus); // BlockEntities
        ModMenus.register(modEventBus);         // GUIs / Containers (we'll create this next)

        // Listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // Config
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Forge Event Bus
        MinecraftForge.EVENT_BUS.register(this);

        // Network
        PacketHandler.register();
        TerritoryEvents.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup complete");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add items/blocks to creative tabs
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        CivilizationManager.load(event.getServer());
        TerritoryManager.load(event.getServer());
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
