package net.reminitous.civilizationsmod.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.reminitous.civilizationsmod.civilization.Civilization;
import net.reminitous.civilizationsmod.territory.TerritoryManager;

import java.util.ArrayList;
import java.util.List;

public class CivilizationStorage {

    private final Civilization civilization;

    public CivilizationStorage(Civilization civilization) {
        this.civilization = civilization;
    }

    /**
     * Returns all containers (chests, furnaces, shulker boxes) inside civilization territory.
     */
    public List<Container> getAllContainers(Level level) {
        List<Container> containers = new ArrayList<>();
        BlockPos min = civilization.getTerritoryMin();
        BlockPos max = civilization.getTerritoryMax();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockEntity be = level.getBlockEntity(pos);

                    if (be instanceof ChestBlockEntity || be instanceof FurnaceBlockEntity) {
                        containers.add((Container) be);
                    }
                }
            }
        }
        return containers;
    }

    /**
     * Deposit an item stack into the civilization storage.
     */
    public boolean depositItem(Level level, ItemStack stack) {
        for (Container container : getAllContainers(level)) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack slot = container.getItem(i);
                if (slot.isEmpty()) {
                    container.setItem(i, stack.copy());
                    return true;
                } else if (ItemStack.isSameItemSameTags(slot, stack)) {
                    int max = slot.getMaxStackSize();
                    int remaining = max - slot.getCount();
                    if (remaining > 0) {
                        int toAdd = Math.min(remaining, stack.getCount());
                        slot.grow(toAdd);
                        stack.shrink(toAdd);
                        if (stack.isEmpty()) return true;
                    }
                }
            }
        }
        return false; // storage full
    }

    /**
     * Withdraw an item stack from the civilization storage.
     */
    public ItemStack withdrawItem(Level level, ItemStack request) {
        for (Container container : getAllContainers(level)) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack slot = container.getItem(i);
                if (ItemStack.isSameItemSameTags(slot, request)) {
                    int toTake = Math.min(slot.getCount(), request.getCount());
                    ItemStack result = slot.split(toTake);
                    return result;
                }
            }
        }
        return ItemStack.EMPTY; // not found
    }

    /**
     * Calculate total "value" of all stored items for war health.
     */
    public long getTotalValue(Level level) {
        long total = 0;
        for (Container container : getAllContainers(level)) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                total += getItemValue(stack) * stack.getCount();
            }
        }
        return total;
    }

    /**
     * Assign a value to an item stack based on rarity or type.
     * This is used for war calculations and can be expanded.
     */
    private int getItemValue(ItemStack stack) {
        // Example simple value system
        switch (stack.getItem().toString()) {
            case "minecraft:diamond":
                return 500;
            case "minecraft:iron_ingot":
                return 50;
            case "minecraft:gold_ingot":
                return 100;
            case "minecraft:stone":
                return 1;
            default:
                return 10;
        }
    }
}
