package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class SetBiomeCommand implements ISubCommand {

    @Override
    public String getName() {
        return "setbiome";
    }

    @Override
    public String getDescription() {
        return "Sets the biome of your island";
    }

    @Override
    public String getSyntax() {
        return "/is setbiome <biome>";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList(
            Arrays
                .stream(Biome.values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .toArray(String[]::new)
        );
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(
                ChatColor.RED + "Invalid syntax use: " + getSyntax()
            );
            return;
        } else if (args.length == 2) {
            try {
                Biome biome = Biome.valueOf(args[1].toUpperCase());
                int islandId = Skyrama
                    .getGridManager()
                    .getIdFromLocation(player.getLocation());
                List<IslandUser> islandUsers = IslandUser.getPlayersForIsland(
                    islandId
                );
                islandUsers.removeIf(i ->
                    !Objects.equals(i.username, player.getName()) ||
                    i.rank == Rank.INVITED
                );
                if (islandUsers.size() > 0) {
                    Skyrama
                        .getSchematicManager()
                        .setRegionBiome(player.getName(), islandId, biome);
                    player.sendMessage(
                        ChatColor.GREEN +
                        "Successfully changed island biome to: " +
                        args[1]
                    );
                    player.sendMessage(
                        ChatColor.GREEN + "Relog to see changes"
                    );
                } else {
                    player.sendMessage(
                        ChatColor.RED + "You don't own an island"
                    );
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "No such biome: " + args[1]);
            }
        }
    }
}
