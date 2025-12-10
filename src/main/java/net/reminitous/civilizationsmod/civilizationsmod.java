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

@Mod(CivilizationsMod.MODID)
public class CivilizationsMod {
    public static final String MODID = "mineciv"; // Keep your original mod ID
    public static final Logger LOGGER = LogUtils.getLogger();

    public CivilizationsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Original setup
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register to Forge event bus
        MinecraftForge.EVENT_BUS.register(this);

        // Original registrations
        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();

        // New registrations from added code
        PacketHandler.register();
        TerritoryEvents.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // You can put additional setup code here if needed
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Your creative tab setup here
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
