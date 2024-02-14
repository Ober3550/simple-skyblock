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
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class IslandManager {

    public IslandManager() {}

    public static Location getConfigLocation(String locationPath) {
        FileConfiguration config = Skyrama.getPlugin(Skyrama.class).getConfig();
        World world = Bukkit.getWorld(config.getString("general.world"));
        int x = 0;
        int y = 0;
        int z = 0;
        try {
            x = Integer.parseInt(config.getString(locationPath + ".X"));
            y = Integer.parseInt(config.getString(locationPath + ".Y"));
            z = Integer.parseInt(config.getString(locationPath + ".Z"));
        } catch (Exception e) {}
        return new Location(world, x, y, z);
    }

    public void createIsland(Player player, int islandId) {
        if (islandId > 0) {
            FileConfiguration config = Skyrama
                .getPlugin(Skyrama.class)
                .getConfig();
            int verticalOffset = Integer.parseInt(
                config.getString("general.spawn.Y")
            );
            Location center = Skyrama
                .getGridManager()
                .getCenterFromId(islandId);

            World world = Bukkit.getWorld(
                Skyrama
                    .getPlugin(Skyrama.class)
                    .getConfig()
                    .getString("general.world")
            );
            Location spawn = new Location(
                world,
                center.getX() + 1.5,
                center.getY() + verticalOffset,
                center.getZ() - 1.5
            );
            Location schematicOffset = getConfigLocation("island.offset");
            Bukkit
                .getLogger()
                .info(
                    "Ofsetting schematic by: " +
                    schematicOffset.getBlockX() +
                    "x " +
                    (schematicOffset.getBlockY() + verticalOffset) +
                    "y " +
                    schematicOffset.getBlockZ() +
                    "z"
                );
            Location schematicLocation = new Location(
                world,
                center.getX() + schematicOffset.getBlockX() + 1,
                center.getY() + schematicOffset.getBlockY() + verticalOffset,
                center.getZ() + schematicOffset.getBlockZ() - 2
            );
            Skyrama
                .getSchematicManager()
                .createIsland(player, schematicLocation);
            Island island = new Island(islandId, center, spawn, true);
            Island.create(islandId);
            island.save();
            IslandUser islandUser = new IslandUser(
                player.getName(),
                islandId,
                Rank.OWNER
            );
            islandUser.create();
            Skyrama
                .getSchematicManager()
                .claimRegion(player.getName(), islandId);
            player.sendMessage(ChatColor.GREEN + "Created island: " + islandId);
            Bukkit.getServer().dispatchCommand(player, "is home");
        }
    }
}
