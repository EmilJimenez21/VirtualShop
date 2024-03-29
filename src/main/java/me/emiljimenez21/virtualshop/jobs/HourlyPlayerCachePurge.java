package me.emiljimenez21.virtualshop.jobs;

import me.emiljimenez21.virtualshop.managers.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

public class HourlyPlayerCachePurge extends BukkitRunnable {
    @Override
    public void run() {
        if(PlayerManager.getPlayers().size() == 0) return;

        Common.log("Removing offline players from the cache");

        PlayerManager.getInstance().cleanUpCache();
    }
}
