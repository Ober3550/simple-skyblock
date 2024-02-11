package fr.kozen.skyrama.storage.queries;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.types.Rank;
import java.sql.*;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class IslandDao {

    public static void createTableUsers() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `islands_users` (" +
                "  `uuid` varchar(255) NOT NULL," +
                "  `island_id` int(11) NOT NULL," +
                "  `rank` int(11) NOT NULL," +
                "PRIMARY KEY (uuid)" +
                ")"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void createTableIslands() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `islands` (" +
                "  `id` int(11) NOT NULL," +
                "  `center_x` float NOT NULL DEFAULT '0'," +
                "  `center_y` float NOT NULL DEFAULT '0'," +
                "  `center_z` float NOT NULL DEFAULT '0'," +
                "  `spawn_x` float NOT NULL DEFAULT '0'," +
                "  `spawn_y` float NOT NULL DEFAULT '0'," +
                "  `spawn_z` float NOT NULL DEFAULT '0'," +
                "  `spawn_yaw` float NOT NULL DEFAULT '0'," +
                "  `spawn_pitch` float NOT NULL DEFAULT '0'," +
                "  `biome` varchar(255) NOT NULL DEFAULT 'TAIGA'," +
                "PRIMARY KEY (id)" +
                ")"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static int getNextId() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT islands.id + 1 AS missing from islands LEFT JOIN islands AS nextIsland ON islands.id + 1 = nextIsland.id WHERE nextIsland.id IS NULL ORDER BY islands.id LIMIT 1;"
            );

            ResultSet rs = stmt.executeQuery();
            int id = 1;
            while (rs.next()) {
                id = rs.getInt("missing");
            }
            if (id > 0) {
                PreparedStatement createStmt = conn.prepareStatement(
                    "INSERT INTO islands(id) VALUES(?);"
                );
                createStmt.setInt(1, id);
                createStmt.executeUpdate();
            }
            return id;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return 0;
    }

    public static Island getIsland(int islandId) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM islands WHERE id = ?"
            );
            stmt.setInt(1, islandId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                World world = Bukkit.getWorld(
                    Skyrama
                        .getPlugin(Skyrama.class)
                        .getConfig()
                        .getString("general.world")
                );
                Location center = new Location(
                    world,
                    resultSet.getFloat("center_x"),
                    resultSet.getFloat("center_y"),
                    resultSet.getFloat("center_z")
                );
                Location spawn = new Location(
                    world,
                    resultSet.getFloat("spawn_x"),
                    resultSet.getFloat("spawn_y"),
                    resultSet.getFloat("spawn_z"),
                    resultSet.getFloat("spawn_yaw"),
                    resultSet.getFloat("spawn_pitch")
                );
                Biome biome = Biome.valueOf(resultSet.getString("biome"));
                Bukkit
                    .getLogger()
                    .info("Successfully loaded island: " + islandId);
                return new Island(islandId, center, spawn, biome);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return null;
    }

    public static Map<OfflinePlayer, Rank> getPlayers(int islandId) {
        Map<OfflinePlayer, Rank> players = new HashMap<>();

        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM islands_users WHERE island_id = ?"
            );
            stmt.setInt(1, islandId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Bukkit
                    .getLogger()
                    .info(
                        Bukkit
                            .getOfflinePlayer(
                                UUID.fromString(resultSet.getString("uuid"))
                            )
                            .getName()
                    );
                players.put(
                    Bukkit.getOfflinePlayer(
                        UUID.fromString(resultSet.getString("uuid"))
                    ),
                    Rank.fromInt(resultSet.getInt("rank"))
                );
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }

        return players;
    }

    public static int getPlayerIsland(Player player) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM islands_users WHERE uuid = ? AND `rank` = ?"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setInt(2, Rank.OWNER.getValue());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Bukkit
                    .getLogger()
                    .info(
                        "Found owner for island: " +
                        resultSet.getInt("island_id")
                    );
                return resultSet.getInt("island_id");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return 0;
    }

    public static void save(Island island) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE islands SET " +
                "center_x = ?, " +
                "center_y = ?, " +
                "center_z = ?, " +
                "spawn_x = ?, " +
                "spawn_y = ?, " +
                "spawn_z = ?, " +
                "spawn_yaw = ?, " +
                "spawn_pitch = ?, " +
                "biome = ? " +
                "WHERE id = ?;"
            );
            stmt.setFloat(1, island.getCenter().getBlockX());
            stmt.setFloat(2, island.getCenter().getBlockY());
            stmt.setFloat(3, island.getCenter().getBlockZ());
            stmt.setFloat(4, island.getSpawn().getBlockX());
            stmt.setFloat(5, island.getSpawn().getBlockY());
            stmt.setFloat(6, island.getSpawn().getBlockZ());
            stmt.setFloat(7, island.getSpawn().getYaw());
            stmt.setFloat(8, island.getSpawn().getPitch());
            stmt.setString(9, String.valueOf(island.getBiome()));
            stmt.setInt(10, island.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void addPlayer(
        OfflinePlayer player,
        Island island,
        Rank rank
    ) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO islands_users(`uuid`, `island_id`, `rank`) VALUES(?, ?, ?);"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setInt(2, island.getId());
            stmt.setInt(3, rank.rank);
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void removePlayer(OfflinePlayer player) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM islands_users WHERE uuid = ?;"
            );
            stmt.setString(1, player.getUniqueId().toString());
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void removeIsland(int islandId) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM islands WHERE id = ?;"
            );
            stmt.setInt(1, islandId);
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }

        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM islands_users WHERE island_id = ?;"
            );
            stmt.setInt(1, islandId);
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void setPlayerIsland(Player player, Island island) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE islands_users SET island_id = ?, rank = ? WHERE uuid = ?;"
            );
            stmt.setInt(1, island.getId());
            stmt.setInt(2, 1);
            stmt.setString(3, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void dropUsers() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DROP TABLE islands_users;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void dropIslands() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DROP TABLE islands;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }
}
