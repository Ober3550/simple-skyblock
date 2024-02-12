package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CreateCommand implements ISubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create an island";
    }

    @Override
    public String getSyntax() {
        return "/is create <optional islandId>";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        islandUsers.removeIf(i -> i.rank != Rank.OWNER);
        // Check if user already owns an island
        if (islandUsers.size() == 0 || player.isOp()) {
            int islandId = 0;
            if (args.length > 1) {
                islandId = Integer.parseInt(args[1]);
                if (Island.availableIslandId(islandId)) {
                    Skyrama.getIslandManager().createIsland(player, islandId);
                } else {
                    player.sendMessage(
                        ChatColor.RED +
                        "Island id: " +
                        islandId +
                        " is not available"
                    );
                }
            } else {
                islandId = Island.getNextId();
                Skyrama.getIslandManager().createIsland(player, islandId);
            }
        } else {
            player.sendMessage(
                Skyrama
                    .getLocaleManager()
                    .getString("player-already-have-island")
            );
        }
    }
}
