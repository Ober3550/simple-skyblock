package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.text.DecimalFormat;
import java.util.*;
import org.bukkit.entity.Player;

public class InfoCommand implements ISubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Prints the information of the current island";
    }

    @Override
    public String getSyntax() {
        return "/is info";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        int islandId = Skyrama
            .getGridManager()
            .getIdFromLocation(player.getLocation());
        if (islandId == 0) {
            player.sendMessage("World Spawn");
        } else {
            Island island = Island.getIsland(islandId);
            player.sendMessage("Island Id: " + islandId);
            if (island != null) {
                List<IslandUser> islandList = IslandUser.getPlayersForIsland(
                    islandId
                );
                DecimalFormat decimals = new DecimalFormat("0.##");
                player.sendMessage(
                    "Center: " +
                    decimals.format(island.center.getX()) +
                    "x " +
                    decimals.format(island.center.getY()) +
                    "y " +
                    decimals.format(island.center.getZ()) +
                    "z"
                );
                player.sendMessage(
                    "Home: " +
                    decimals.format(island.spawn.getX()) +
                    "x " +
                    decimals.format(island.spawn.getY()) +
                    "y " +
                    decimals.format(island.spawn.getZ()) +
                    "z"
                );
                player.sendMessage(
                    "Visitors: " +
                    (island.allowVisitors ? "enabled" : "disabled")
                );
                player.sendMessage("Members:");
                for (IslandUser islandUser : islandList) {
                    player.sendMessage(
                        islandUser.username + " Rank: " + islandUser.rank
                    );
                }
            }
        }
    }
}
