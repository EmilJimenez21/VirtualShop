package me.emiljimenez21.virtualshop.jobs;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

public class CheckForUpdates extends BukkitRunnable {
    @Override
    public void run() {
        Common.log("Checking for plugin updates on Spigot...");
        Virtualshop.updater.run();
    }
}
