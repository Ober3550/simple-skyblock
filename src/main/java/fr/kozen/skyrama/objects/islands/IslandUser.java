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
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class IslandUser {

    public int id;
    public String username;
    public int islandId;
    public Rank rank;

    public IslandUser(String username, int islandId, Rank rank) {
        this.username = username;
        this.islandId = islandId;
        this.rank = rank;
    }

    public static void createTable() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `island_users` (" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                "  `username` varchar(255) NOT NULL," +
                "  `island_id` int(11) NOT NULL," +
                "  `rank` int(11) NOT NULL," +
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
                "DROP TABLE island_users;"
            );
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public void create() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO island_users(`username`, `island_id`, `rank`) VALUES(?, ?, ?);"
            );
            stmt.setString(1, this.username);
            stmt.setInt(2, this.islandId);
            stmt.setInt(3, this.rank.getValue());
            stmt.execute();
            if (this.rank == Rank.MEMBER) {
                Skyrama
                    .getSchematicManager()
                    .addMemberToRegion(this.username, this.islandId);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public void delete() {
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM island_users WHERE username = ? AND island_id = ? AND `rank` = ?;"
            );
            stmt.setString(1, this.username);
            stmt.setInt(2, this.islandId);
            stmt.setInt(3, this.rank.getValue());
            stmt.execute();
            if (this.rank == Rank.MEMBER) {
                Skyrama
                    .getSchematicManager()
                    .removeMemberFromRegion(this.username, this.islandId);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
    }

    public static List<IslandUser> getIslandsForPlayer(String username) {
        List<IslandUser> islandUsers = new ArrayList<IslandUser>();
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM island_users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int islandId = resultSet.getInt("island_id");
                Rank rank = Rank.fromInt(resultSet.getInt("rank"));
                islandUsers.add(new IslandUser(username, islandId, rank));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return islandUsers;
    }

    public static List<IslandUser> getPlayersForIsland(int islandId) {
        List<IslandUser> islandUsers = new ArrayList<IslandUser>();
        try {
            Connection conn = Skyrama.getSqlManager().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM island_users WHERE island_id = ?"
            );
            stmt.setInt(1, islandId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                Rank rank = Rank.fromInt(resultSet.getInt("rank"));
                islandUsers.add(new IslandUser(username, islandId, rank));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("Something went wrong. " + e);
        }
        return islandUsers;
    }
}
