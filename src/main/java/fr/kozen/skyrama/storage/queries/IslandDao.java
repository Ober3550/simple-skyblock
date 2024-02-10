package fr.kozen.skyrama.storage.queries;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.types.Rank;
import java.sql.*;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class IslandDao {

    public static void createTableUsers() {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `islands_users` (" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                "  `uuid` varchar(255) NOT NULL," +
                "  `island_id` int(11) NOT NULL," +
                "  `rank` int(11) NOT NULL," +
                "PRIMARY KEY (id)" +
                ")"
            )
        ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void createTableIslands() {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `islands` (" +
                "  `id` int(11) NOT NULL," +
                "  `biome` varchar(255) NOT NULL DEFAULT 'TAIGA'," +
                "  `spawn_x` float NOT NULL DEFAULT '0'," +
                "  `spawn_y` float NOT NULL DEFAULT '0'," +
                "  `spawn_z` float NOT NULL DEFAULT '0'," +
                "  `spawn_yaw` float NOT NULL DEFAULT '0'," +
                "  `spawn_pitch` float NOT NULL DEFAULT '0'," +
                "PRIMARY KEY (id)" +
                ")"
            )
        ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static int getNextId() {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT islands.id + 1 AS missing from islands LEFT JOIN islands AS nextIsland ON islands.id + 1 = nextIsland.id WHERE nextIsland.id IS NULL ORDER BY islands.id LIMIT 1;"
            )
        ) {
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

    public static Set<Island> getIslands() {
        Bukkit.getLogger().info("Gettings islands...");

        Set<Island> islands = new HashSet<>();

        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM islands"
            )
        ) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Island island = new Island(
                    resultSet.getInt("id"),
                    Biome.valueOf(resultSet.getString("biome")),
                    new Location(
                        Bukkit.getWorld(
                            Skyrama
                                .getPlugin(Skyrama.class)
                                .getConfig()
                                .getString("general.world")
                        ),
                        resultSet.getFloat("spawn_x"),
                        resultSet.getFloat("spawn_y"),
                        resultSet.getFloat("spawn_z"),
                        resultSet.getFloat("spawn_yaw"),
                        resultSet.getFloat("spawn_pitch")
                    )
                );

                islands.add(island);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }

        return islands;
    }

    public static Map<OfflinePlayer, Rank> getPlayers(int islandId) {
        Map<OfflinePlayer, Rank> players = new HashMap<>();

        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM islands_users WHERE island_id = ?"
            )
        ) {
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

    public static void save(Island island) {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE islands SET biome = ?, spawn_x = ?, spawn_y = ?, spawn_z = ?, spawn_yaw = ?, spawn_pitch = ? WHERE id = ?;"
            )
        ) {
            stmt.setString(1, String.valueOf(island.getBiome()));
            stmt.setFloat(2, island.getSpawn().getBlockX());
            stmt.setFloat(3, island.getSpawn().getBlockY());
            stmt.setFloat(4, island.getSpawn().getBlockZ());
            stmt.setFloat(5, island.getSpawn().getYaw());
            stmt.setFloat(6, island.getSpawn().getPitch());
            stmt.setInt(7, island.getId());

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
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO islands_users(`uuid`, `island_id`, `rank`) VALUES(?, ?, ?);"
            )
        ) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setInt(2, island.getId());
            stmt.setInt(3, rank.rank);
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void removePlayer(OfflinePlayer player) {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM islands_users WHERE uuid = ?;"
            )
        ) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void setPlayerIsland(Player player, Island island) {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE islands_users SET island_id = ?, rank = ? WHERE uuid = ?;"
            )
        ) {
            stmt.setInt(1, island.getId());
            stmt.setInt(2, 1);
            stmt.setString(3, player.getUniqueId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void dropUsers() {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DROP TABLE islands_users;"
            )
        ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void dropIslands() {
        try (
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DROP TABLE islands;"
            )
        ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }
}
