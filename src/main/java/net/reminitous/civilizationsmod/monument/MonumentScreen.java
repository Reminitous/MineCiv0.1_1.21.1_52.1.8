package net.reminitous.civilizationsmod.Monument;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

public class MonumentMenu extends AbstractContainerMenu {

    private final BlockEntity blockEntity;

    @ObjectHolder("civilizationsmod:monument_menu")
    public static MenuType<MonumentMenu> TYPE;

    public MonumentMenu(int windowId, Inventory playerInventory, BlockEntity blockEntity) {
        super(TYPE, windowId);
        this.blockEntity = blockEntity;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }
}
