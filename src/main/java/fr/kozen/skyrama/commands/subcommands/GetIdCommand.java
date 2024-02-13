package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GetIdCommand implements ISubCommand {

    @Override
    public String getName() {
        return "id";
    }

    @Override
    public String getDescription() {
        return "Gets the id of the current plot";
    }

    @Override
    public String getSyntax() {
        return "/is id";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList();
    }

    @Override
    public void perform(Player player, String[] args) {
        Location loc = player.getLocation();
        player.sendMessage(
            "Id for plot: " +
            loc.getBlockX() +
            "x " +
            loc.getBlockZ() +
            "z is: " +
            Skyrama.getGridManager().getIdFromLocation(loc)
        );
    }
}
