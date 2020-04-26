package me.emiljimenez21.virtualshop.jobs;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckForUpdates extends BukkitRunnable {
    @Override
    public void run() {
        Virtualshop.getUpdater().run();
    }
}
