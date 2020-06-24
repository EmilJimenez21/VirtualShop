package me.emiljimenez21.virtualshop.jobs;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

public class ReportMetrics extends BukkitRunnable {
    @Override
    public void run() {
        Common.log("Sending server information to the developer");
        new Thread(() -> {
            try {
                // Send the data
                Virtualshop.getReport().sendServerData();
            } catch (final Exception e) {
                // Do Nothing
            }
        }).start();

    }
}
