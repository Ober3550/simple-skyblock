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

public class AllowVisitorsCommand implements ISubCommand {

    @Override
    public String getName() {
        return "visitors";
    }

    @Override
    public String getDescription() {
        return "Sets whether people are allowed to visit";
    }

    @Override
    public String getSyntax() {
        return "/is visitors <enabled/disabled>";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList("enabled", "disabled");
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            if (args.length == 1) {
                player.sendMessage(
                    ChatColor.RED + "Invalid syntax use: " + getSyntax()
                );
                return;
            } else if (args.length > 1) {
                if (islandUsers.size() == 1) {
                    boolean allowVisitors = true;
                    if ("enabled".equals(args[1])) {
                        allowVisitors = true;
                    } else if ("disabled".equals(args[1])) {
                        allowVisitors = false;
                    } else {
                        player.sendMessage(
                            ChatColor.RED +
                            "The Arguments are: enabled/disabled"
                        );
                        return;
                    }
                    IslandUser islandUser = islandUsers.get(0);
                    Island island = Island.getIsland(islandUser.islandId);
                    island.allowVisitors = allowVisitors;
                    island.save();
                    player.sendMessage(
                        ChatColor.GREEN +
                        "Allow visitors: " +
                        (island.allowVisitors ? "enabled" : "disabled")
                    );
                }
            }
        } catch (Exception e) {
            String msg = "Failed to set allow visitors:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
