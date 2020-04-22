package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.ShopUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager {
    private static volatile PlayerManager instance;
    private static HashMap<String, ShopUser> players = new HashMap<String, ShopUser>();
    private static HashMap<String, Long> player_expiration = new HashMap<String, Long>();

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
            Objects.requireNonNull(instance, "Cannot get a new instance! Have you reloaded?");
        }
        return instance;
    }

    public static ShopUser getPlayer(String uuid) {
        ShopUser p = players.get(uuid);
        if(p == null) {
            ResultSet res = Virtualshop.db.getDatabase().getPlayer(uuid);
            try {
                p = new ShopUser(res.getString("uuid"), res.getString("name"));
            } catch (Exception e) {
                // Do Nothing
            }
        }
        return p;
    }

    public static void addPlayer(String uuid, ShopUser player){
        players.put(uuid, player);
        player_expiration.put(uuid, System.currentTimeMillis() + 3600 * 1000);
    }

    public static void addPlayer(Player player) {
        ResultSet res = Virtualshop.db.getDatabase().getPlayer(player.getName());
        if(res == null) {
            Virtualshop.db.getDatabase().createPlayer(player.getUniqueId().toString(), player.getName());
        }

        addPlayer(player.getUniqueId().toString(), new ShopUser(player));
    }

    public static void removePlayer(String uuid) {
        players.remove(uuid);
        player_expiration.remove(uuid);
    }

    public static HashMap<String, ShopUser> getPlayers() {
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
