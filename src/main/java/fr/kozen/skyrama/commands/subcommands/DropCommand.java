package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.storage.queries.IslandDao;
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
        if ("islands".equalsIgnoreCase(args[1])) {
            IslandDao.dropIslands();
            IslandDao.createTableIslands();
            player.sendMessage("Dumped table islands");
        } else if ("users".equalsIgnoreCase(args[1])) {
            IslandDao.dropUsers();
            IslandDao.createTableUsers();
            player.sendMessage("Dumped table users");
        }
    }
}
