package net.reminitous.civilizationsmod.monument;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class MonumentMenu extends AbstractContainerMenu {

    protected MonumentMenu(int id, Inventory inv) {
        super(null, id); // MenuType to be registered
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // Add slots and buttons for managing civilization: add/remove members, alliances, war
}
