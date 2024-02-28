package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import java.util.Objects;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InviteCommand implements ISubCommand {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Invite player to join your island";
    }

    @Override
    public String getSyntax() {
        return "/is invite <add/accept/deny>";
    }

    @Override
    public List<String> getArgs(Player player) {
        return Arrays.asList("add", "accept", "decline");
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            if (args.length < 3) {
                player.sendMessage(
                    ChatColor.RED + "Invalid syntax use: " + getSyntax()
                );
                return;
            }

            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                player.sendMessage("Could not find player: " + args[2]);
                return;
            }

            if ("add".equals(args[1])) {
                List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
                    player.getName()
                );
                islandUsers.removeIf(i -> i.rank != Rank.OWNER);
                if (islandUsers.size() == 0) {
                    player.sendMessage(
                        ChatColor.RED +
                        "You don't have an island to invite someone to"
                    );
                    return;
                }
                IslandUser owner = islandUsers.get(0);
                List<IslandUser> existingInvites = IslandUser.getIslandsForPlayer(
                    target.getName()
                );
                existingInvites.removeIf(i -> i.islandId != owner.islandId);
                if (existingInvites.size() > 0) {
                    player.sendMessage(
                        ChatColor.RED +
                        target.getName() +
                        " has already been invited or is already a member of the island"
                    );
                    return;
                }
                target.sendMessage(
                    player.getName() + " invited you to play on their island!"
                );
                TextComponent messageYes = new TextComponent(
                    ChatColor.GREEN + "[ACCEPT] "
                );
                messageYes.setClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/is invite accept " + player.getName()
                    )
                );

                TextComponent messageNo = new TextComponent(
                    ChatColor.RED + "[DECLINE] "
                );
                messageNo.setClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/is invite decline " + player.getName()
                    )
                );
                IslandUser invite = new IslandUser(
                    target.getName(),
                    owner.islandId,
                    Rank.INVITED
                );
                invite.create();
                player.sendMessage("Sent invite to: " + target.getName());
                messageYes.addExtra(messageNo);
                target.spigot().sendMessage(messageYes);
            } else if ("accept".equals(args[1])) {
                List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(
                    args[2]
                );
                islandUsers.removeIf(i -> i.rank != Rank.OWNER);
                if (islandUsers.size() == 0) {
                    player.sendMessage(
                        ChatColor.RED + args[2] + " doesn't own an island"
                    );
                    return;
                }
                IslandUser ownerIsland = islandUsers.get(0);
                List<IslandUser> invitedCheck = IslandUser.getIslandsForPlayer(
                    player.getName()
                );
                invitedCheck.removeIf(i ->
                    i.rank != Rank.INVITED || i.islandId != ownerIsland.islandId
                );
                if (invitedCheck.size() == 0) {
                    player.sendMessage(
                        ChatColor.RED +
                        "You can't accept an invite you never received"
                    );
                    return;
                }
                invitedCheck.get(0).delete();
                IslandUser newIslandUser = new IslandUser(
                    player.getName(),
                    ownerIsland.islandId,
                    Rank.MEMBER
                );
                newIslandUser.create();
                player.sendMessage("Accepted invite!");
            } else if ("decline".equals(args[1])) {
                player.sendMessage("You declined their invite");
            }
        } catch (Exception e) {
            String msg = "Failed invite player to island:" + e;
            player.sendMessage(ChatColor.RED + msg);
            Bukkit.getLogger().info(msg);
        }
    }
}
