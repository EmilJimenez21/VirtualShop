package me.emiljimenez21.virtualshop.jobs;

import me.emiljimenez21.virtualshop.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncOnlinePlayers extends BukkitRunnable {

    @Override
    public void run() {
        // Load all online players into the cache
        for(Player p: Bukkit.getOnlinePlayers()){
            PlayerManager.addPlayer(p);
        }
    }
}
