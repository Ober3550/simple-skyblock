package fr.kozen.skyrama.objects.islands;

import fr.kozen.skyrama.storage.queries.IslandDao;
import fr.kozen.skyrama.types.Rank;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class Island {

    private int id;
    private Location center;
    private Location spawn;
    private Biome biome;
    private Map<OfflinePlayer, Rank> players;
    private Map<Player, Player> invites;

    public Island(int id, Location center, Location spawn, Biome biome) {
        this.id = id;
        this.center = center;
        this.spawn = spawn;
        this.biome = biome;
        this.players = IslandDao.getPlayers(id);
        this.invites = new HashMap<>();
    }

    public boolean createIsland(Player player) {
        return true;
    }

    public boolean deleteIsland(Player player) {
        return true;
    }

    public int getId() {
        return this.id;
    }

    public Biome getBiome() {
        return this.biome;
    }

    public OfflinePlayer getOwner() {
        return this.getPlayers()
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().equals(Rank.OWNER))
            .findAny()
            .get()
            .getKey();
    }

    public Rank getRank(OfflinePlayer player) {
        return this.getPlayers().get(this.getPlayer(player));
    }

    public OfflinePlayer getPlayer(OfflinePlayer player) {
        return this.getPlayers()
            .keySet()
            .stream()
            .filter(offlinePlayer ->
                offlinePlayer.getUniqueId().equals(player.getUniqueId())
            )
            .findAny()
            .orElse(null);
    }

    public void removePlayer(OfflinePlayer player) {
        this.getPlayers().remove(player);
        IslandDao.removePlayer(player);
    }

    public void addPlayer(OfflinePlayer player, Rank rank) {
        this.getPlayers().put(player, rank);
        IslandDao.addPlayer(player, this, rank);
    }

    public Map<OfflinePlayer, Rank> getPlayers() {
        return this.players;
    }

    public Map<Player, Player> getInvites() {
        return this.invites;
    }

    public Location getCenter() {
        return this.center;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void save() {
        IslandDao.save(this);
    }
}
