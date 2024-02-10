package fr.kozen.skyrama;

import fr.kozen.skyrama.commands.CommandManager;
import fr.kozen.skyrama.events.*;
import fr.kozen.skyrama.objects.grids.GridManager;
import fr.kozen.skyrama.objects.islands.IslandManager;
import fr.kozen.skyrama.objects.locales.LocaleManager;
import fr.kozen.skyrama.objects.schematics.SchematicManager;
import fr.kozen.skyrama.storage.SqlManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skyrama extends JavaPlugin {

    private static GridManager gridManager;
    private static SqlManager sqlManager;
    private static IslandManager islandManager;
    private static SchematicManager schematicManager;
    private static LocaleManager localeManager;

    @Override
    public void onEnable() {
        this.initConfig();
        this.initObjects();
        this.initEvents();
        this.initCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initConfig() {
        // Load default config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        // Load default locales
        saveResource("locales/en_US.yml", false);
        saveResource("locales/de_DE.yml", false);
        saveResource("schematics/island.schem", false);
    }

    public void initCommands() {
        getCommand("island").setExecutor(new CommandManager());
    }

    public void initObjects() {
        gridManager = new GridManager();
        sqlManager = new SqlManager();
        islandManager = new IslandManager();
        schematicManager = new SchematicManager();
        localeManager = new LocaleManager();

        sqlManager.populate();
        islandManager.loadIslands();
    }

    public void initEvents() {
        getServer()
            .getPluginManager()
            .registerEvents(new OnPlayerRespawn(), this);
    }

    public static GridManager getGridManager() {
        return gridManager;
    }

    public static SqlManager getSqlManager() {
        return sqlManager;
    }

    public static IslandManager getIslandManager() {
        return islandManager;
    }

    public static SchematicManager getSchematicManager() {
        return schematicManager;
    }

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }
}
