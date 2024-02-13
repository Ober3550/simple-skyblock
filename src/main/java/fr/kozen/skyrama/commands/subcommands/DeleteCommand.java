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

public class DeleteCommand implements ISubCommand {

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Deletes your island";
    }

    @Override
    public String getSyntax() {
        return "/is delete";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        int islandId = 0;
        if (
            args.length > 1 &&
            "current".equalsIgnoreCase(args[1]) &&
            player.isOp()
        ) {
            islandId =
                Skyrama
                    .getGridManager()
                    .getIdFromLocation(player.getLocation());
        }
        List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
            player.getName()
        );
        islandUsers.removeIf(i -> i.rank != Rank.OWNER);
        if (islandUsers.size() == 0) {
            player.sendMessage(
                ChatColor.RED + "You don't have an island to delete"
            );
            return;
        } else if (islandUsers.size() == 1) {
            islandId = islandUsers.get(0).islandId;
        } else if (islandUsers.size() > 1) {
            int locationId = Skyrama
                .getGridManager()
                .getIdFromLocation(player.getLocation());
            islandUsers.removeIf(i -> i.islandId != locationId);
            if (islandUsers.size() == 1) {
                islandId = islandUsers.get(0).islandId;
            } else {
                player.sendMessage(
                    ChatColor.RED +
                    "If you own more than one island stand on the island you want to delete"
                );
                return;
            }
        }
        if (islandId > 0) {
            Island island = Island.getIsland(islandId);
            player.sendMessage("Deleted island: " + islandId);
            if (Skyrama.getGridManager().playerIsOnIsland(player, islandId)) {
                Bukkit.getServer().dispatchCommand(player, "is spawn");
            }
            Skyrama
                .getSchematicManager()
                .deleteRegion(player.getName(), islandId);
        }
    }
}
