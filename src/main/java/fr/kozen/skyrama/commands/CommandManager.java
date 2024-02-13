package fr.kozen.skyrama.commands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.commands.subcommands.*;
import fr.kozen.skyrama.interfaces.ISubCommand;
import java.util.*;
import javax.annotation.Nullable;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    public final List<ISubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new DeleteCommand());
        subcommands.add(new ListCommand());
        subcommands.add(new GetIdCommand());

        subcommands.add(new LeaveCommand());
        subcommands.add(new RemoveCommand());
        subcommands.add(new AllowVisitorsCommand());
        subcommands.add(new InviteCommand());

        subcommands.add(new InfoCommand());
        subcommands.add(new KickCommand());
        subcommands.add(new VisitCommand());
        subcommands.add(new SetBiomeCommand());
        subcommands.add(new SetHomeCommand());
        subcommands.add(new HomeCommand());
        subcommands.add(new CreateCommand());
        subcommands.add(new SpawnCommand());
        // subcommands.add(new DropCommand());
        this.initTabCompleter();
    }

    @Override
    public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
    ) {
        if (args.length > 0) {
            for (int i = 0; i < subcommands.size(); i++) {
                if (args[0].equalsIgnoreCase(subcommands.get(i).getName())) {
                    subcommands.get(i).perform((Player) sender, args);
                }
            }
        } else if (args.length == 0) {
            sender.sendMessage(
                ChatColor.YELLOW + "--------------------------------"
            );
            for (int i = 0; i < subcommands.size(); i++) {
                sender.sendMessage(
                    ChatColor.GOLD +
                    subcommands.get(i).getSyntax() +
                    ": " +
                    ChatColor.WHITE +
                    subcommands.get(i).getDescription()
                );
            }
            sender.sendMessage(
                ChatColor.YELLOW + "--------------------------------"
            );
        }
        return true;
    }

    public void initTabCompleter() {
        Skyrama
            .getPlugin(Skyrama.class)
            .getCommand("island")
            .setTabCompleter(
                new TabCompleter() {
                    @Override
                    public @Nullable List<String> onTabComplete(
                        @NotNull CommandSender sender,
                        @NotNull Command command,
                        @NotNull String alias,
                        @NotNull String[] args
                    ) {
                        List<String> l = new ArrayList<>();
                        if (args.length == 1) {
                            subcommands.forEach(subCommand -> {
                                l.add(subCommand.getName());
                            });
                        } else if (args.length == 2) {
                            ISubCommand subCommand = subcommands
                                .stream()
                                .filter(subCommandFilter ->
                                    subCommandFilter.getName().equals(args[0])
                                )
                                .findAny()
                                .orElse(null);
                            if (subCommand != null) {
                                subCommand
                                    .getArgs((Player) sender)
                                    .forEach(arg -> {
                                        l.add(arg);
                                    });
                            }
                        }
                        return l;
                    }
                }
            );
    }
}
