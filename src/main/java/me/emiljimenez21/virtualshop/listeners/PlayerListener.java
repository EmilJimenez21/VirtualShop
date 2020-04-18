package me.emiljimenez21.virtualshop.listeners;

import me.emiljimenez21.virtualshop.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerManager.addPlayer(e.getPlayer());
    }

}
