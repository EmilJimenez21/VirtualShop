package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class JobManager {
    private List<BukkitRunnable> jobs = new ArrayList<>();

    public void addAsyncJob(BukkitRunnable job, int seconds) {
        job.runTaskTimerAsynchronously(Virtualshop.getInstance(), 0, seconds * 20);
    }

}
