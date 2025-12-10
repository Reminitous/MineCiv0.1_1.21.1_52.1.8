package net.reminitous.civilizationsmod.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.monument.MonumentMenu;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CivilizationsMod.MODID);

    public static final RegistryObject<MenuType<MonumentMenu>> MONUMENT_MENU = MENUS.register("monument_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MonumentMenu(windowId, inv, inv.player.level.getBlockEntity(data.readBlockPos()))));
}
