package fr.kozen.skyrama.objects.islands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class IslandManager {

    public IslandManager() {}

    public void createIsland(Player owner, int islandId) {
        if (islandId > 0) {
            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN + "Creating island id: " + islandId
                );
            Location center = Skyrama
                .getGridManager()
                .getCenterFromId(islandId);

            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN +
                    "At location centred on: " +
                    center.getX() +
                    "x " +
                    center.getY() +
                    "y " +
                    center.getZ() +
                    "z"
                );
            int test;

            Location spawn = new Location(
                Bukkit.getWorld(
                    Skyrama
                        .getPlugin(Skyrama.class)
                        .getConfig()
                        .getString("general.world")
                ),
                center.getBlockX() + 1.5,
                center.getBlockY() - 58,
                center.getBlockZ() - 1.5
            );

            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN +
                    "Setting island home to: " +
                    spawn.getX() +
                    "x " +
                    spawn.getY() +
                    "y " +
                    spawn.getZ() +
                    "z"
                );

            Island island = new Island(
                islandId,
                center,
                spawn,
                Biome.TAIGA,
                true
            );
            Island.create(islandId);
            island.save();

            IslandUser islandUser = new IslandUser(
                owner.getName(),
                islandId,
                Rank.OWNER
            );
            islandUser.create();

            Skyrama
                .getSchematicManager()
                .createIsland(
                    center.getX() + 4,
                    center.getY() + 5 - 58,
                    center.getZ() + 3
                );
            Skyrama
                .getSchematicManager()
                .claimRegion(owner.getName(), islandId);
            owner.getPlayer().teleport(spawn);
        }
    }
}
