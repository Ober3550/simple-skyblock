package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements ISubCommand {

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getDescription() {
        return "Teleport to world spawn";
    }

    @Override
    public String getSyntax() {
        return "/is spawn";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        FileConfiguration config = Skyrama.getPlugin(Skyrama.class).getConfig();
        World world = Bukkit.getWorld(config.getString("general.world"));
        int x = 0;
        int y = 0;
        int z = 0;
        try {
            x = Integer.parseInt(config.getString("general.spawn.X"));
            y = Integer.parseInt(config.getString("general.spawn.Y"));
            z = Integer.parseInt(config.getString("general.spawn.Z"));
        } catch (Exception e) {}
        Location location = new Location(world, x, y, z);
        player.teleport(location);
    }
}
