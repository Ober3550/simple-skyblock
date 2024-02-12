package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.block.Biome;
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
    public String getPermission() {
        return "skyrama.command.info";
    }

    @Override
    public String getSyntax() {
        return "/island info";
    }

    @Override
    public List<String> getArgs() {
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
            List<IslandUser> islandList = IslandUser.getPlayersForIsland(
                islandId
            );
            player.sendMessage("Info for island: " + islandId);
            player.sendMessage(
                "Centered on: " +
                island.center.getX() +
                "x " +
                island.center.getY() +
                "y " +
                island.center.getZ() +
                "z"
            );
            player.sendMessage(
                "Home on: " +
                island.spawn.getX() +
                "x " +
                island.spawn.getY() +
                "y " +
                island.spawn.getZ() +
                "z"
            );
            player.sendMessage("Biome: " + String.valueOf(island.biome));
            player.sendMessage(
                "Allow Visitors: " +
                (island.allowVisitors ? "enabled" : "disabled")
            );
            for (IslandUser islandUser : islandList) {
                player.sendMessage(
                    "User: " +
                    islandUser.username +
                    " Island: " +
                    islandUser.islandId +
                    " Rank: " +
                    islandUser.rank
                );
            }
        }
    }
}
