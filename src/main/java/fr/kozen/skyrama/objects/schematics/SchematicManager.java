package fr.kozen.skyrama.objects.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SchematicManager {

    public SchematicManager() {
        this.initialise();
    }

    public void initialise() {
        Plugin worldEditPlugin = Bukkit
            .getPluginManager()
            .getPlugin("WorldEdit");
    }

    public void createIsland(Player player, Location location) {
        FileConfiguration config = Skyrama.getPlugin(Skyrama.class).getConfig();
        String worldString = config.getString("general.world");
        Bukkit
            .getServer()
            .dispatchCommand(
                Bukkit.getConsoleSender(),
                "/world " + worldString
            );
        Bukkit
            .getServer()
            .dispatchCommand(
                Bukkit.getConsoleSender(),
                "/pos1 " +
                location.getBlockX() +
                "," +
                location.getBlockY() +
                "," +
                location.getBlockZ()
            );
        Bukkit
            .getServer()
            .dispatchCommand(
                Bukkit.getConsoleSender(),
                "/schematic load island"
            );
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "/paste");
    }

    public void selectRegion(String username, int islandId) {
        FileConfiguration config = Skyrama.getPlugin(Skyrama.class).getConfig();
        String worldString = config.getString("general.world");
        World world = Bukkit.getWorld(worldString);

        int offset =
            Integer.parseInt(config.getString("island.plotsize")) * 16 / 2;
        Location center = Skyrama.getGridManager().getCenterFromId(islandId);
        BlockVector3 start = BlockVector3.at(
            center.getBlockX() - offset,
            -64,
            center.getBlockZ() - offset
        );
        BlockVector3 end = BlockVector3.at(
            center.getBlockX() + offset,
            320,
            center.getBlockZ() + offset
        );

        Server server = Bukkit.getServer();
        server.dispatchCommand(
            Bukkit.getConsoleSender(),
            "/world " + worldString
        );
        server.dispatchCommand(
            Bukkit.getConsoleSender(),
            "/pos1 " +
            start.getBlockX() +
            "," +
            start.getBlockY() +
            "," +
            start.getBlockZ()
        );
        server.dispatchCommand(
            Bukkit.getConsoleSender(),
            "/pos2 " +
            end.getBlockX() +
            "," +
            end.getBlockY() +
            "," +
            end.getBlockZ()
        );
    }

    public void claimRegion(String username, int islandId) {
        if (islandId > 0) {
            selectRegion(username, islandId);
            Server server = Bukkit.getServer();
            server.dispatchCommand(
                Bukkit.getConsoleSender(),
                "rg define island" + islandId + " " + username
            );
            server.dispatchCommand(
                Bukkit.getConsoleSender(),
                "rg flag island" +
                islandId +
                " greeting Welcome to " +
                username +
                "'s Island!"
            );
        }
    }

    public void addMemberToRegion(String username, int islandId) {
        if (islandId > 0) {
            selectRegion(username, islandId);
            Server server = Bukkit.getServer();
            server.dispatchCommand(
                Bukkit.getConsoleSender(),
                "rg addmember island" + islandId + " " + username
            );
        }
    }

    public void removeMemberFromRegion(String username, int islandId) {
        if (islandId > 0) {
            selectRegion(username, islandId);
            Bukkit
                .getServer()
                .dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "rg removemember island" + islandId + " " + username
                );
        }
    }

    public void deleteRegion(String username, int islandId) {
        if (islandId > 0) {
            World world = Bukkit.getWorld(
                Skyrama
                    .getPlugin(Skyrama.class)
                    .getConfig()
                    .getString("general.world")
            );
            FileConfiguration config = Skyrama
                .getPlugin(Skyrama.class)
                .getConfig();
            int offset =
                Integer.parseInt(config.getString("island.plotsize")) / 2;
            Location center = Skyrama
                .getGridManager()
                .getCenterFromId(islandId);
            int startX = center.getBlockX() / 16 - offset;
            int endX = center.getBlockX() / 16 + offset;
            int startZ = center.getBlockZ() / 16 - offset;
            int endZ = center.getBlockZ() / 16 + offset;
            int count = 0;
            for (int i = startX; i < endX; i++) {
                for (int j = startZ; j < endZ; j++) {
                    count++;
                    world.regenerateChunk(i, j);
                }
            }
            Bukkit.getLogger().info("Chunks deleted: " + count);
            Bukkit
                .getServer()
                .dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "rg delete island" + islandId
                );
            Island.delete(islandId);
        }
    }

    public void setRegionBiome(String username, int islandId, Biome biome) {
        if (islandId > 0) {
            selectRegion(username, islandId);
            Server server = Bukkit.getServer();
            server.dispatchCommand(
                Bukkit.getConsoleSender(),
                "/setbiome " + String.valueOf(biome).toLowerCase()
            );
        }
    }
}
