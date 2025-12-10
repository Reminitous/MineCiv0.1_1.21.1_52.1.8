package net.reminitous.civilizationsmod.menus;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;

/**
 * Registers all custom container menus (GUIs) for the Civilizations Mod.
 */
public class ModMenus {

    // Deferred register for container menus
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, CivilizationsMod.MOD_ID);

    /**
     * Monument Management Menu
     * Used by leaders to manage their civilization.
     */
    public static final RegistryObject<MenuType<MonumentMenu>> MONUMENT_MENU =
            MENUS.register("monument_menu",
                    () -> new MenuType<>(MonumentMenu::new));

    // Add other menus here in the future if needed
}
