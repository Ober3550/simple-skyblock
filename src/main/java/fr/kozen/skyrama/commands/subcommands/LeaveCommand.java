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
        List<IslandUser> islands = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        if (islands.size() == 1) {
            IslandUser islandUser = islands.get(0);
            if (islandUser.rank == Rank.OWNER) {
                player.sendMessage(
                    ChatColor.RED + "Island owners cannot leave. Use /is delete"
                );
            } else {
                islandUser.delete();
                player.sendMessage(
                    ChatColor.GREEN + "Successfully left island"
                );
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
                        "You are not part of island: " +
                        islandId
                    );
                }
            } else {
                for (IslandUser islandUser : islands) {
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
        } else {
            player.sendMessage(ChatColor.RED + "You aren't part of an island");
        }
    }
}
