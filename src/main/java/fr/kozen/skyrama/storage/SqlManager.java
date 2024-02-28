package fr.kozen.skyrama.storage;

import fr.kozen.skyrama.Skyrama;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.bukkit.Bukkit;

public class SqlManager {

    private Connection conn;

    public SqlManager() {
        try {
            String url =
                "jdbc:sqlite:" +
                Skyrama.getPlugin(Skyrama.class).getDataFolder() +
                "/islands.db";
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            Bukkit.getLogger().info("Failed to create sqlite database:" + e);
        }
    }

    public Connection getConnection() throws SQLException {
        return conn;
    }

    @Override
    protected void finalize() {
        try {
            conn.close();
        } catch (Exception e) {}
    }
}
