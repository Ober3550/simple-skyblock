package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import java.util.Arrays;
import java.util.List;
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
    public String getPermission() {
        return "skyrama.command.spawn";
    }

    @Override
    public String getSyntax() {
        return "/island spawn";
    }

    @Override
    public List<String> getArgs() {
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
