package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListCommand implements ISubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List island ids player is added to";
    }

    @Override
    public String getSyntax() {
        return "/is list";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            List<IslandUser> islandList = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            player.sendMessage("Listing islands...");
            for (IslandUser islandUser : islandList) {
                player.sendMessage(
                    "Island: " +
                    islandUser.islandId +
                    " Rank: " +
                    islandUser.rank
                );
            }
        } catch (Exception e) {
            String msg =
                "Failed to get list of islands players is added to:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
