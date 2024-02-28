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

public class RemoveCommand implements ISubCommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove a player from the island";
    }

    @Override
    public String getSyntax() {
        return "/is remove <player>";
    }

    @Override
    public List<String> getArgs(Player player) {
        try {
            List<String> islandMembers = new ArrayList<String>();
            List<IslandUser> islands = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            islands.removeIf(i -> i.rank != Rank.OWNER);
            if (islands.size() > 0) {
                IslandUser owner = islands.get(0);
                List<IslandUser> targetIslands = IslandUser.getPlayersForIsland(
                    owner.islandId
                );
                for (IslandUser user : targetIslands) {
                    islandMembers.add(user.username);
                }
            }
            return islandMembers;
        } catch (Exception e) {
            String msg = "Failed to get list of players to remove:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
        return null;
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
            List<IslandUser> islands = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            islands.removeIf(i -> i.rank != Rank.OWNER);
            if (islands.size() > 0) {
                IslandUser owner = islands.get(0);
                List<IslandUser> targetIslands = IslandUser.getIslandsForPlayer(
                    args[1]
                );
                if (targetIslands.size() > 0) {
                    IslandUser target = targetIslands.get(0);
                    if (target.rank == Rank.OWNER) {
                        player.sendMessage(
                            ChatColor.RED + "Cannot remove owner from island"
                        );
                    } else {
                        target.delete();
                        player.sendMessage(
                            ChatColor.GREEN +
                            "Successfully removed " +
                            args[1] +
                            " from island"
                        );
                    }
                } else {
                    player.sendMessage(
                        ChatColor.RED + "Player is not member of this island"
                    );
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't own an island");
            }
        } catch (Exception e) {
            String msg = "Failed to remove player from island:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
