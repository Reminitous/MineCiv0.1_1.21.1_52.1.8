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

// Registry imports - ALL in correct packages
import net.reminitous.civilizationsmod.registry.ModBlockEntities;
import net.reminitous.civilizationsmod.registry.ModItems;
import net.reminitous.civilizationsmod.registry.ModEntities;
import net.reminitous.civilizationsmod.registry.ModMenus;
import net.reminitous.civilizationsmod.registry.ModSounds;

// Manager imports
import net.reminitous.civilizationsmod.civilization.CivilizationManager;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

// Network imports
import net.reminitous.civilizationsmod.network.packets.ModMessages;

// Event handler imports
import net.reminitous.civilizationsmod.handlers.TerritoryEventHandler;
import net.reminitous.civilizationsmod.events.CivilizationServerEvents;

/**
 * Main mod class for Civilizations Mod.
 * Handles initialization, registration, and event setup.
 */
@Mod(CivilizationsMod.MODID)
public class CivilizationsMod {

    // Mod ID - MUST match gradle.properties
    public static final String MODID = "civilizationsmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Mod event bus reference
    public static IEventBus MOD_BUS;

    /**
     * Mod constructor - called during mod loading.
     * This is where all registration happens.
     */
    public CivilizationsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MOD_BUS = modEventBus;

        LOGGER.info("Initializing Civilizations Mod...");

        // ===========================
        // DEFERRED REGISTER SETUP
        // ===========================
        // These MUST be called before any events fire

        net.reminitous.civilizationsmod.registry.ModBlocks.BLOCKS.register(modEventBus);
        LOGGER.debug("Registered blocks");

        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        LOGGER.debug("Registered block entities");

        ModItems.ITEMS.register(modEventBus);
        LOGGER.debug("Registered items");

        ModEntities.ENTITIES.register(modEventBus);
        LOGGER.debug("Registered entities");

        ModMenus.MENUS.register(modEventBus);
        LOGGER.debug("Registered menus");

        ModSounds.SOUNDS.register(modEventBus);
        LOGGER.debug("Registered sounds");

        // ===========================
        // MOD EVENT BUS LISTENERS
        // ===========================

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        LOGGER.debug("Registered mod event listeners");

        // ===========================
        // CONFIG
        // ===========================

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        LOGGER.debug("Registered config");

        // ===========================
        // FORGE EVENT BUS
        // ===========================

        // Register this class for server events
        MinecraftForge.EVENT_BUS.register(this);

        // Register event handlers
        //MinecraftForge.EVENT_BUS.register(new TerritoryEventHandler());
        MinecraftForge.EVENT_BUS.register(new CivilizationServerEvents());

        LOGGER.debug("Registered Forge event listeners");

        // ===========================
        // NETWORK
        // ===========================

        ModMessages.register();
        LOGGER.debug("Registered network packets");

        LOGGER.info("Civilizations Mod initialization complete!");
    }

    /**
     * Common setup event - runs after registration.
     * Use this for initialization that needs registered objects.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup starting...");

        event.enqueueWork(() -> {
            // Any thread-safe initialization here
            LOGGER.info("Common setup work complete");
        });
    }

    /**
     * Add items to creative tabs.
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add custom items to creative tabs here if needed
        // Example:
        // if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
        //     event.accept(ModBlocks.MONUMENT);
        // }
    }

    /**
     * Server starting event - load persistent data.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting - loading civilization data...");

        // Load manager data
        CivilizationManager.load(event.getServer());
        TerritoryManager.load(event.getServer());

        LOGGER.info("Civilization data loaded");
    }

    /**
     * Client-side event handler.
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup starting...");
            LOGGER.info("Player: {}", Minecraft.getInstance().getUser().getName());

            // Register client-side renderers, screens, etc. here
            event.enqueueWork(() -> {
                // Example: Register screens
                // MenuScreens.register(ModMenus.MONUMENT_MENU.get(), MonumentScreen::new);
            });
        }
    }
}