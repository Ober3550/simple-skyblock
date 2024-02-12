package fr.kozen.skyrama.events;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.objects.islands.IslandUser;
import fr.kozen.skyrama.types.Rank;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class OnPlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String username = event.getPlayer().getName();
        List<IslandUser> islandUsers = IslandUser.getIslandsForPlayer(username);
        if (islandUsers.size() > 1) {
            islandUsers.removeIf(i -> i.rank != Rank.OWNER);
            // If the user is part of multiple islands but not an owner just spawn them at the first one
            if (islandUsers.size() == 0) {
                islandUsers = IslandUser.getIslandsForPlayer(username);
            }
        }
        if (islandUsers.size() > 0) {
            IslandUser islandUser = islandUsers.get(0);
            Island island = Island.getIsland(islandUser.islandId);
            event.setRespawnLocation(island.spawn);
        }
    }
}
