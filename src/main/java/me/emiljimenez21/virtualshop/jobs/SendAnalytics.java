package me.emiljimenez21.virtualshop.jobs;

import com.google.gson.JsonObject;
import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

public class SendAnalytics extends BukkitRunnable {
    @Override
    public void run() {
        if(Settings.reporting) {
            JsonObject data = Virtualshop.getAnalytics().getData();
            Virtualshop.getReport().sendCommangUsage(data);
        }

        Virtualshop.getAnalytics().reset();
    }
}
