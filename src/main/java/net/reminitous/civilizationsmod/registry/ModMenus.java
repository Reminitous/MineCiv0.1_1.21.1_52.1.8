package net.reminitous.civilizationsmod.registry;

import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.menu.MonumentMenu;

@Mod.EventBusSubscriber(modid = CivilizationsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModMenus {

    // Deferred register for menu types
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CivilizationsMod.MODID);

    // Register MonumentMenu
    public static final RegistryObject<MenuType<MonumentMenu>> MONUMENT_MENU =
            MENUS.register("monument_menu", () -> new MenuType<>(MonumentMenu.FACTORY));

    // Call this method in your mod constructor to attach the DeferredRegister to the mod event bus
    public static void register() {
        MENUS.register(CivilizationsMod.MOD_BUS);
    }
}
