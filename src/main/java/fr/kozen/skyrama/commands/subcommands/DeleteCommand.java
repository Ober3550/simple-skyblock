package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
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
        if (args.length > 1) {
            if (player.isOp()) {
                int islandId = 0;
                if ("current".equalsIgnoreCase(args[1])) {
                    islandId =
                        Skyrama
                            .getGridManager()
                            .getIdFromLocation(player.getLocation());
                } else {
                    islandId = Integer.parseInt(args[1]);
                }
                Skyrama.getSchematicManager().deleteRegion(player, islandId);
            }
        } else {
            Island island = Skyrama.getIslandManager().getPlayerIsland(player);
            if (island != null) {
                Skyrama
                    .getSchematicManager()
                    .deleteRegion(player, island.getId());
                Bukkit.getServer().dispatchCommand(player, "island spawn");
            } else {
                player.sendMessage(
                    ChatColor.RED + "You don't have an island to delete"
                );
            }
        }
    }
}