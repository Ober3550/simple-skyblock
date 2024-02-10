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
        int plotsize = Integer.parseInt(
            Skyrama
                .getPlugin(Skyrama.class)
                .getConfig()
                .getString("island.plotsize")
        );
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

    public int isInPlayerIsland(Player player, Location location) {
        if (
            location.getWorld() ==
            Bukkit.getWorld(
                Skyrama
                    .getPlugin(Skyrama.class)
                    .getConfig()
                    .getString("general.world")
            )
        ) {
            if (Skyrama.getIslandManager().getPlayerIsland(player) != null) {
                Island island = Skyrama
                    .getIslandManager()
                    .getPlayerIsland(player);
                Location center = getCenterFromId(island.getId());
                int plotsize = Integer.parseInt(
                    Skyrama
                        .getPlugin(Skyrama.class)
                        .getConfig()
                        .getString("island.plotsize")
                );

                int minX = center.getBlockX() - plotsize;
                int maxX = center.getBlockX() + plotsize;

                int minZ = center.getBlockZ() - plotsize;
                int maxZ = center.getBlockZ() + plotsize;

                if (
                    location.getBlockX() >= minX && location.getBlockX() <= maxX
                ) {
                    if (
                        location.getBlockZ() >= minZ &&
                        location.getBlockZ() <= maxZ
                    ) {
                        return 2;
                    }
                }
                return 1;
            }
            return 1;
        }
        return 0;
    }
}
