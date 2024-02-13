package fr.kozen.skyrama.interfaces;

import java.util.*;
import org.bukkit.entity.Player;

public interface ISubCommand {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract List<String> getArgs(Player player);

    public abstract void perform(Player player, String args[]);
}
