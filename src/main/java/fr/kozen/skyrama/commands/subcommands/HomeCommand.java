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

public class HomeCommand implements ISubCommand {

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Teleport to island home";
    }

    @Override
    public String getSyntax() {
        return "/is home";
    }

    @Override
    public List<String> getArgs(Player player) {
        List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        List<String> islandLocations = new ArrayList<String>();
        for (IslandUser islandUser : islandUsers) {
            islandLocations.add("" + islandUser.islandId);
        }
        return islandLocations;
    }

    @Override
    public void perform(Player player, String[] args) {
        List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        if (islandUsers.size() > 1) {
            islandUsers.removeIf(i -> i.rank != Rank.OWNER);
        }
        if (islandUsers.size() > 1) {
            if (args.length > 1) {
                int islandId = Integer.parseInt(args[1]);
                islandUsers.removeIf(i -> i.islandId != islandId);
                if (islandUsers.size() == 1) {
                    Island island = Island.getIsland(
                        islandUsers.get(0).islandId
                    );
                    player.teleport(island.spawn);
                }
            } else {
                player.sendMessage(
                    ChatColor.RED +
                    "You have more than one island please specify which to go home to"
                );
                for (IslandUser island : islandUsers) {
                    player.sendMessage("Island: " + island.islandId);
                }
            }
        }
        if (islandUsers.size() == 1) {
            Island island = Island.getIsland(islandUsers.get(0).islandId);
            player.teleport(island.spawn);
        }
    }
}
