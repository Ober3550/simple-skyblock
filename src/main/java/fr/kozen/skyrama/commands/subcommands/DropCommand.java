package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import java.util.Arrays;
import java.util.List;
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
    public String getPermission() {
        return "skyrama.command.drop";
    }

    @Override
    public String getSyntax() {
        return "/island drop";
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        Island.dropTable();
        Island.createTable();
        IslandUser.dropTable();
        IslandUser.createTable();

        player.sendMessage("Dumped and reconstructed tables");
    }
}
