package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KickCommand implements ISubCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kick a player from your island";
    }

    @Override
    public String getSyntax() {
        return "/is kick <player>";
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
            if (args.length != 2) {
                player.sendMessage(
                    ChatColor.RED + "Invalid syntax use: " + getSyntax()
                );
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage("Could not find player: " + args[1]);
                return;
            }
            List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            islandUsers.removeIf(i -> i.rank != Rank.OWNER);
            if (islandUsers.size() > 0) {
                IslandUser islandUser = islandUsers.get(0);
                int islandId = islandUser.islandId;
                if (
                    Skyrama.getGridManager().playerIsOnIsland(target, islandId)
                ) {
                    Bukkit.getServer().dispatchCommand(target, "is spawn");
                } else {
                    player.sendMessage(
                        ChatColor.RED + "They are not on your island"
                    );
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't own an island");
            }
        } catch (Exception e) {
            String msg = "Failed to kick player from island:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
