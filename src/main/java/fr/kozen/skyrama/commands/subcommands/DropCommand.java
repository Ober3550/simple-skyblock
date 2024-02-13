package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.*;
import org.bukkit.entity.Player;

public class DropCommand implements ISubCommand {

    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public String getDescription() {
        return "Drop database tables";
    }

    @Override
    public String getSyntax() {
        return "/is drop";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        if (player.isOp()) {
            Island.dropTable();
            Island.createTable();
            IslandUser.dropTable();
            IslandUser.createTable();

            player.sendMessage("Dumped and reconstructed tables");
        }
    }
}
