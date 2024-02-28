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

public class SetHomeCommand implements ISubCommand {

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getDescription() {
        return "Sets the home of your island to your position";
    }

    @Override
    public String getSyntax() {
        return "/is sethome";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int islandId = Skyrama
                .getGridManager()
                .getIdFromLocation(player.getLocation());
            List<IslandUser> islandUsers = IslandUser.getPlayersForIsland(
                islandId
            );
            islandUsers.removeIf(i ->
                !Objects.equals(i.username, player.getName()) ||
                i.islandId != islandId
            );
            if (islandUsers.size() == 1) {
                Island island = Island.getIsland(islandId);
                island.spawn = player.getLocation();
                island.save();
                player.sendMessage(
                    Skyrama.getLocaleManager().getString("setspawn-success")
                );
            } else {
                player.sendMessage(ChatColor.RED + "Failed to sethome");
            }
        } catch (Exception e) {
            String msg = "Failed to set home:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
