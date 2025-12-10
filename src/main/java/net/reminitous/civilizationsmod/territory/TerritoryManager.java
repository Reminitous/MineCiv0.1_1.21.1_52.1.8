package net.reminitous.civilizationsmod.territory;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.reminitous.civilizationsmod.civilization.Civilization;

import java.util.Random;

public class TerritoryManager {

    private static final Random random = new Random();

    /**
     * Check if coordinates are inside a civilization's claimed territory.
     */
    public static boolean isInsideCivilization(double x, double y, double z, Civilization civ) {
        if (civ == null) return false;

        BlockPos min = civ.getTerritoryMin();
        BlockPos max = civ.getTerritoryMax();

        return x >= min.getX() && x <= max.getX()
                && y >= min.getY() && y <= max.getY()
                && z >= min.getZ() && z <= max.getZ();
    }

    /**
     * Return a random valid position inside a civilization's territory.
     */
    public static BlockPos getRandomPositionInTerritory(ServerLevel level, Civilization civ) {
        if (civ == null) return null;

        BlockPos min = civ.getTerritoryMin();
        BlockPos max = civ.getTerritoryMax();

        int attempts = 20; // limit to avoid infinite loops
        while (attempts-- > 0) {
            int x = random.nextInt(max.getX() - min.getX() + 1) + min.getX();
            int z = random.nextInt(max.getZ() - min.getZ() + 1) + min.getZ();
            int y = level.getHeight() - 1; // start at max height

            // Find the highest solid block at (x, z)
            while (y > 0 && !level.getBlockState(new BlockPos(x, y, z)).getMaterial().isSolid()) {
                y--;
            }

            BlockPos spawnPos = new BlockPos(x, y + 1, z);
            if (level.isEmptyBlock(spawnPos)) {
                return spawnPos;
            }
        }

        return null; // failed to find a valid position
    }
}
