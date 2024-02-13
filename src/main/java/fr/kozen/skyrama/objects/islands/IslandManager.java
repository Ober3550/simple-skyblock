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

    public void createIsland(Player owner, int islandId) {
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

            Location schematicLocation = new Location(
                world,
                center.getX(),
                center.getY() + verticalOffset,
                center.getZ()
            );

            // Island island = new Island(islandId, center, spawn, true);
            // Island.create(islandId);
            // island.save();
            // IslandUser islandUser = new IslandUser(
            //     owner.getName(),
            //     islandId,
            //     Rank.OWNER
            // );
            // islandUser.create();
            Skyrama
                .getSchematicManager()
                .createIsland(owner, schematicLocation);

            Skyrama
                .getSchematicManager()
                .claimRegion(owner.getName(), islandId);
            owner.getPlayer().teleport(spawn);
            owner.sendMessage(ChatColor.GREEN + "Created island: " + islandId);
        }
    }
}
