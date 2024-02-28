package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.*;
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
    public String getSyntax() {
        return "/is visit <player>";
    }

    @Override
    public List<String> getArgs(Player player) {
        List<String> usernames = new ArrayList<String>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            usernames.add(onlinePlayer.getName());
        }
        return usernames;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            if (args.length == 1) {
                player.sendMessage(
                    ChatColor.RED + "Invalid syntax use: " + getSyntax()
                );
                return;
            } else if (args.length > 1) {
                String username = args[1];
                List<IslandUser> islands = IslandUser.getIslandsForPlayer(
                    username
                );
                if (islands.size() == 0) {
                    player.sendMessage(
                        ChatColor.RED + "Player does not own an island"
                    );
                } else if (islands.size() == 1) {
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
                    // TODO implement visiting a player that owns multiple islands
                    player.sendMessage(
                        ChatColor.RED +
                        "Visiting a player that owns multiple islands is not yet implemented"
                    );
                }
            }
        } catch (Exception e) {
            String msg = "Failed to visit player:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
