package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VisitCommand implements ISubCommand {

    @Override
    public String getName() {
        return "visit";
    }

    @Override
    public String getDescription() {
        return "Visit player island";
    }

    @Override
    public String getPermission() {
        return "skyrama.command.visit";
    }

    @Override
    public String getSyntax() {
        return "/island visit {player}";
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            String username = args[1];
            List<IslandUser> islands = IslandUser.getIslandsForPlayer(username);
            if (islands.size() == 1) {
                Island island = Island.getIsland(islands.get(0).islandId);
                if (island.allowVisitors) {
                    player.teleport(island.spawn);
                } else {
                    player.sendMessage(
                        ChatColor.RED +
                        username +
                        " is not allowing visitors at this time"
                    );
                }
            } else {
                player.sendMessage(
                    "Player owns more than one island. Specify which to visit"
                );
            }
        }
    }
}
