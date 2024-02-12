package fr.kozen.skyrama.objects.grids;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GridManager {

    public GridManager() {
        this.initialise();
    }

    public void initialise() {}

    public Location getCenterFromId(int id) {
        int plotsize =
            Integer.parseInt(
                Skyrama
                    .getPlugin(Skyrama.class)
                    .getConfig()
                    .getString("island.plotsize")
            ) *
            16;
        int x = 0;
        int z = 0;
        int state = 0;
        int turnSteps = 1;
        int turnCounter = 1;

        for (int step = 1; step <= id; step++) {
            switch (state) {
                case 0:
                    x += plotsize;
                    break;
                case 1:
                    z -= plotsize;
                    break;
                case 2:
                    x -= plotsize;
                    break;
                case 3:
                    z += plotsize;
                    break;
            }

            if (step % turnSteps == 0) {
                state = (state + 1) % 4;
                if (turnCounter % 2 == 0) {
                    turnSteps++;
                }
                turnCounter++;
            }
        }

        return new Location(Bukkit.getWorld("world"), x, 0, z);
    }

    public int getIdFromLocation(Location location) {
        int plotsize =
            Integer.parseInt(
                Skyrama
                    .getPlugin(Skyrama.class)
                    .getConfig()
                    .getString("island.plotsize")
            ) *
            16;
        // x = 2, z = 1
        int x = Math.round((float) location.getBlockX() / (float) plotsize);
        int z = Math.round((float) location.getBlockZ() / (float) plotsize);
        int absx = Math.abs(x);
        int absz = Math.abs(z);
        int halfEdge = Math.max(absx, absz) - 1;
        int edge = 2 * halfEdge + 1;
        int square = edge * edge;
        if (absx > absz) {
            // If we're along a +- x row
            if (x > 0) {
                return square + 0 * (edge + 1) + (halfEdge - z);
            } else {
                return square + 2 * (edge + 1) + (halfEdge + z);
            }
        } else if (absz > absx) {
            // If we're along a +- z row
            if (z < 0) {
                return square + 1 * (edge + 1) + (halfEdge - x);
            } else {
                return square + 3 * (edge + 1) + (halfEdge + x);
            }
        } else if (absx == absz) {
            // If we're on a corner
            if (x > 0 && z < 0) {
                return square + 1 * (edge + 1) - 1;
            }
            if (x < 0 && z < 0) {
                return square + 2 * (edge + 1) - 1;
            }
            if (x < 0 && z > 0) {
                return square + 3 * (edge + 1) - 1;
            }
            if (x > 0 && z > 0) {
                return square + 4 * (edge + 1) - 1;
            }
        }
        return 0;
    }
}
