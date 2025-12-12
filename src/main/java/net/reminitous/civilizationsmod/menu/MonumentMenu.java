package net.reminitous.civilizationsmod.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.NetworkHooks;
import net.reminitous.civilizationsmod.registry.ModMenus;

/*public class MonumentMenu extends AbstractContainerMenu {

    private final SimpleContainer inventory;

    // Server-side constructor
    public MonumentMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(9));
    }

    public MonumentMenu(int id, Inventory playerInventory, SimpleContainer container) {
        super(ModMenus.MONUMENT_MENU.get(), id);
        this.inventory = container;

        // Add Monument inventory slots (3x3)
        int slotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(inventory, slotIndex++, 44 + col * 18, 17 + row * 18));
            }
        }

        // Add player inventory slots (3x9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add hotbar slots (1x9)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            if (index < inventory.getContainerSize()) {
                if (!this.moveItemStackTo(stackInSlot, inventory.getContainerSize(), this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stackInSlot, 0, inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return stack;
    }
}


    public static void openScreen(ServerPlayer player, SimpleContainer container, BlockPos pos) {
        NetworkHooks.openScreen(player, new MonumentMenuProvider(container, pos), pos);
    }


    private static class MonumentMenuProvider implements net.minecraft.world.inventory.MenuProvider {
        private final SimpleContainer container;
        private final BlockPos pos;

        public MonumentMenuProvider(SimpleContainer container, BlockPos pos) {
            this.container = container;
            this.pos = pos;
        }

        @Override
        public Component getDisplayName() {
            return Component.literal("Monument");
        }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
            return new MonumentMenu(id, playerInventory, container);
        }
    }


    public static final IContainerFactory<MonumentMenu> FACTORY = (id, inv, buf) -> {
        SimpleContainer container = new SimpleContainer(9);
        return new MonumentMenu(id, inv, container);
    };
*/