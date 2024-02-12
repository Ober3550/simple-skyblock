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
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
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

    public void createIsland(double x, double y, double z) {
        try {
            String filepath =
                Skyrama.getPlugin(Skyrama.class).getDataFolder() +
                "\\schematics\\island.schem";
            File file = new File(filepath);
            Bukkit.getLogger().info("Loading schematic from: " + filepath);
            if (file.exists()) {
                ClipboardFormat format = ClipboardFormats.findByAlias("schem");
                ClipboardReader reader = format.getReader(
                    Files.newInputStream(file.toPath())
                );
                Clipboard clipboard = reader.read();
                try (
                    EditSession editSession = WorldEdit
                        .getInstance()
                        .getEditSessionFactory()
                        .getEditSession(
                            BukkitAdapter.adapt(
                                Objects.requireNonNull(
                                    Bukkit.getWorld(
                                        (String) Skyrama
                                            .getPlugin(Skyrama.class)
                                            .getConfig()
                                            .get("general.world")
                                    )
                                )
                            ),
                            -1
                        )
                ) {
                    Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(
                            BlockVector3.at(
                                x -
                                Math.floor(
                                    clipboard.getDimensions().getX() / 2
                                ),
                                y -
                                Math.floor(
                                    clipboard.getDimensions().getY() / 2
                                ),
                                z -
                                Math.floor(clipboard.getDimensions().getZ() / 2)
                            )
                        )
                        .ignoreAirBlocks(false)
                        .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void claimRegion(Player player, int islandId) {
        FileConfiguration config = Skyrama.getPlugin(Skyrama.class).getConfig();
        String worldString = config.getString("general.world");
        World world = Bukkit.getWorld(worldString);

        int offset = Integer.parseInt(config.getString("island.plotsize")) / 2;
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
        server.dispatchCommand(
            Bukkit.getConsoleSender(),
            "rg define island" + islandId + " " + player.getName()
        );
        try {
            Thread.sleep(100);
        } catch (Exception e) {}
        server.dispatchCommand(
            Bukkit.getConsoleSender(),
            "rg flag island" +
            islandId +
            " greeting Welcome to " +
            player.getName() +
            "'s Island!"
        );
    }

    public void deleteRegion(Player player, int islandId) {
        if (islandId > 0) {
            FileConfiguration config = Skyrama
                .getPlugin(Skyrama.class)
                .getConfig();
            String worldString = config.getString("general.world");
            World world = Bukkit.getWorld(worldString);

            int offset =
                Integer.parseInt(config.getString("island.plotsize")) / 2;
            Location center = Skyrama
                .getGridManager()
                .getCenterFromId(islandId);
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
            server.dispatchCommand(Bukkit.getConsoleSender(), "/set 0");
            server.dispatchCommand(
                Bukkit.getConsoleSender(),
                "rg delete island" + islandId
            );
            Island.delete(islandId);
        }
    }
}
