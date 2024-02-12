package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.Arrays;
import java.util.List;
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
    public String getPermission() {
        return "skyrama.command.list";
    }

    @Override
    public String getSyntax() {
        return "/island list";
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        List<IslandUser> islandList = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        player.sendMessage("Listing islands...");
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
