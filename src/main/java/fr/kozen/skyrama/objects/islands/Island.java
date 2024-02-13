package fr.kozen.skyrama.objects.islands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.types.Rank;
import java.sql.*;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Island {

    public int id;
    public Location center;
    public Location spawn;
    public boolean allowVisitors;

    public Island(
        int id,
        Location center,
        Location spawn,
        boolean allowVisitors
    ) {
        this.id = id;
        this.center = center;
        this.spawn = spawn;
        this.allowVisitors = allowVisitors;
    }

    public static void createTable() {
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
                "  `allow_visitors` char(1) DEFAULT 'T'," +
                "PRIMARY KEY (id)" +
                ")"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static void dropTable() {
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

    public static boolean create(int islandId) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO islands(id) VALUES(?);"
            );
            stmt.setInt(1, islandId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return false;
    }

    public static boolean delete(int islandId) {
        try {
            // Delete island properties
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM islands WHERE id = ?;"
            );
            stmt.setInt(1, islandId);
            stmt.execute();

            // Delete all permissions for users on island
            PreparedStatement users_stmt = conn.prepareStatement(
                "DELETE FROM island_users WHERE island_id = ?;"
            );
            users_stmt.setInt(1, islandId);
            users_stmt.execute();
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return false;
    }

    public static boolean availableIslandId(int islandId) {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM islands WHERE id = ?"
            );
            stmt.setInt(1, islandId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return true;
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

    public void save() {
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
                "allow_visitors = ? " +
                "WHERE id = ?;"
            );
            stmt.setFloat(1, (float) this.center.getX());
            stmt.setFloat(2, (float) this.center.getY());
            stmt.setFloat(3, (float) this.center.getZ());
            stmt.setFloat(4, (float) this.spawn.getX());
            stmt.setFloat(5, (float) this.spawn.getY());
            stmt.setFloat(6, (float) this.spawn.getZ());
            stmt.setFloat(7, this.spawn.getYaw());
            stmt.setFloat(8, this.spawn.getPitch());
            stmt.setString(9, this.allowVisitors ? "T" : "F");
            stmt.setInt(10, this.id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
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
                boolean allowVisitors = "T".equals(
                            resultSet.getString("allow_visitors")
                        )
                    ? true
                    : false;
                return new Island(islandId, center, spawn, allowVisitors);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return null;
    }
}
