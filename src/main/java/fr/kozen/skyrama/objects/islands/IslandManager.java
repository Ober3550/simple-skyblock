package fr.kozen.skyrama.objects.islands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.storage.queries.IslandDao;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;

public class IslandManager {

    public Set<Island> islands;

    public IslandManager() {
        this.initialise();
    }

    public void initialise() {
        this.islands = new HashSet<>();
    }

    public void loadIslands() {
        this.islands = IslandDao.getIslands();
        this.islands.forEach(island ->
                Bukkit.getLogger().info(String.valueOf(island.getId()))
            );
    }

    public void create(OfflinePlayer owner) {
        int islandId = IslandDao.getNextId();
        if (islandId > 0) {
            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN + "Creating island id: " + islandId
                );
            Location location = Skyrama
                .getGridManager()
                .getCenterFromId(islandId);

            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN +
                    "At location centred on: " +
                    location.getX() +
                    "x " +
                    location.getY() +
                    "y " +
                    location.getZ() +
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
                location.getBlockX() + 1.5,
                location.getBlockY() - 58,
                location.getBlockZ() - 1.5
            );

            owner
                .getPlayer()
                .sendMessage(
                    ChatColor.GREEN +
                    "Setting island home to: " +
                    location.getX() +
                    "x " +
                    location.getY() +
                    "y " +
                    location.getZ() +
                    "z"
                );

            Island island = new Island(islandId, Biome.TAIGA, spawn);
            this.islands.add(island);
            island.addPlayer(owner, Rank.OWNER);
            island.setSpawn(spawn);
            island.save();

            Skyrama
                .getSchematicManager()
                .createIsland(
                    location.getX() + 4,
                    location.getY() + 5 - 58,
                    location.getZ() + 3
                );
            owner.getPlayer().teleport(spawn);
        }
    }

    public Island getIslandOwnedBy(OfflinePlayer player) {
        return this.getIslands()
            .stream()
            .filter(island ->
                island.getOwner().getUniqueId().equals(player.getUniqueId())
            )
            .findAny()
            .orElse(null);
    }

    public Island getPlayerIsland(OfflinePlayer player) {
        return this.getIslands()
            .stream()
            .filter(island ->
                island
                    .getPlayers()
                    .keySet()
                    .stream()
                    .filter(offlinePlayer ->
                        offlinePlayer.getUniqueId().equals(player.getUniqueId())
                    )
                    .findAny()
                    .orElse(null) !=
                null
            )
            .findAny()
            .orElse(null);
    }

    public Set<Island> getIslands() {
        return this.islands;
    }
}
