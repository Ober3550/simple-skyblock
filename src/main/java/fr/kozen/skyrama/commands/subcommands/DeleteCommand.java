package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.Arrays;
import java.util.List;
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
    public String getPermission() {
        return "skyrama.command.delete";
    }

    @Override
    public String getSyntax() {
        return "/island delete";
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        int islandId = 0;
        if (args.length > 1) {
            if (player.isOp()) {
                if ("current".equalsIgnoreCase(args[1])) {
                    islandId =
                        Skyrama
                            .getGridManager()
                            .getIdFromLocation(player.getLocation());
                } else {
                    islandId = Integer.parseInt(args[1]);
                }
                player.sendMessage("Deleted island: " + islandId);
                Skyrama.getSchematicManager().deleteRegion(player, islandId);
            }
        } else {
            List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
                player.getName()
            );
            islandUsers.removeIf(i -> i.rank != Rank.OWNER);
            if (islandUsers.size() == 1) {
                islandId = islandUsers.get(0).islandId;
                Skyrama.getSchematicManager().deleteRegion(player, islandId);
                player.sendMessage("Deleted island: " + islandId);
                Bukkit.getServer().dispatchCommand(player, "island spawn");
            } else if (islandUsers.size() > 1) {
                int locationId = Skyrama
                    .getGridManager()
                    .getIdFromLocation(player.getLocation());
                islandUsers.removeIf(i -> i.islandId != locationId);
                if (islandUsers.size() == 1) {
                    islandId = islandUsers.get(0).islandId;
                    Skyrama
                        .getSchematicManager()
                        .deleteRegion(player, islandId);
                    player.sendMessage("Deleted island: " + islandId);
                    Bukkit.getServer().dispatchCommand(player, "island spawn");
                } else {
                    player.sendMessage(
                        ChatColor.RED +
                        "If you own more than one island stand on the island you own to delete"
                    );
                }
            } else {
                player.sendMessage(
                    ChatColor.RED + "You don't have an island to delete"
                );
            }
        }
    }
}
