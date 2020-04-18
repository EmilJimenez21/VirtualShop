package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.ShopPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager {
    private static volatile PlayerManager instance;
    private static HashMap<String, ShopPlayer> players = new HashMap<String, ShopPlayer>();
    private static HashMap<String, Long> player_expiration = new HashMap<String, Long>();

    public PlayerManager() {
        BukkitRunnable job = new BukkitRunnable() {
            @Override
            public void run() {
                asyncJob();
            }
        };
        job.runTaskTimerAsynchronously(Virtualshop.getInstance(), 0,60 * 60 * 20);
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
            Objects.requireNonNull(instance, "Cannot get a new instance! Have you reloaded?");
        }
        return instance;
    }

    public static ShopPlayer getPlayer(String uuid) {
        ShopPlayer p = players.get(uuid);
        if(p == null) {
            ResultSet res = Virtualshop.db.getPlayer(uuid);
            try {
                p = new ShopPlayer(res.getString("uuid"), res.getString("name"));
            } catch (Exception e) {
                // Do Nothing
            }
        }
        return p;
    }

    public static void addPlayer(String uuid, ShopPlayer player){
        players.put(uuid, player);
        player_expiration.put(uuid, System.currentTimeMillis() + 3600 * 1000);
    }

    public static void addPlayer(Player player) {
        ResultSet res = Virtualshop.db.getPlayer(player.getName());
        if(res == null) {
            Virtualshop.db.createPlayer(player.getUniqueId().toString(), player.getName());
        }

        addPlayer(player.getUniqueId().toString(), new ShopPlayer(player));
    }

    public static void removePlayer(String uuid) {
        players.remove(uuid);
        player_expiration.remove(uuid);
    }

    public static HashMap<String, ShopPlayer> getPlayers() {
        return players;
    }

    public void asyncJob() {
        for(Map.Entry<String, Long> player_expires : player_expiration.entrySet()) {
            // Check to see if the player is online
            if(Bukkit.getPlayer(UUID.fromString(player_expires.getKey())) == null) {
                player_expiration.remove(player_expires.getKey());
                player_expiration.put(player_expires.getKey(), System.currentTimeMillis() + 3600 * 1000);
                continue;
            }
            removePlayer(player_expires.getKey());
        }
    }

}
