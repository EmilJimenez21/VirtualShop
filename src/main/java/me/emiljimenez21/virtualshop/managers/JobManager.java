package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.scheduler.BukkitRunnable;

public class JobManager {

    public void runAsyncRepetitiveJob(BukkitRunnable job, int delay, int period) {
        job.runTaskTimerAsynchronously(Virtualshop.getInstance(), delay * 20, period * 20);
    }

    public void runAsyncJob(BukkitRunnable job) {
        job.runTaskAsynchronously(Virtualshop.getInstance());
    }

}
