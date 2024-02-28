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

public class LeaveCommand implements ISubCommand {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leave the current island";
    }

    @Override
    public String getSyntax() {
        return "/is leave <optional islandId>";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            List<IslandUser> islands = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            if (islands.size() == 1) {
                IslandUser islandUser = islands.get(0);
                int islandId = islandUser.islandId;
                if (islandUser.rank == Rank.OWNER) {
                    player.sendMessage(
                        ChatColor.RED +
                        "Island owners cannot leave. Use /is delete"
                    );
                } else {
                    islandUser.delete();
                    player.sendMessage(
                        ChatColor.GREEN + "Successfully left island"
                    );
                    if (
                        Skyrama
                            .getGridManager()
                            .playerIsOnIsland(player, islandId)
                    ) {
                        Bukkit.getServer().dispatchCommand(player, "is spawn");
                    }
                }
            } else if (islands.size() > 1) {
                islands.removeIf(i -> i.rank == Rank.OWNER);
                if (args.length > 1) {
                    int islandId = Integer.parseInt(args[1]);
                    islands.removeIf(i -> i.islandId != islandId);
                    if (islands.size() == 1) {
                        islands.get(0).delete();
                    } else {
                        player.sendMessage(
                            ChatColor.RED +
                            "You are not member of island: " +
                            islandId
                        );
                    }
                } else {
                    player.sendMessage("You are a member of the following:");
                    for (IslandUser islandUser : islands) {
                        player.sendMessage(
                            "Island: " +
                            islandUser.islandId +
                            " Rank: " +
                            islandUser.rank
                        );
                    }
                }
            } else {
                player.sendMessage(
                    ChatColor.RED + "You are not a member of an island"
                );
            }
        } catch (Exception e) {
            String msg = "Failed to leave island:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
