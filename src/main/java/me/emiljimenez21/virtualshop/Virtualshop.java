package me.emiljimenez21.virtualshop;

import com.google.common.collect.Lists;
import me.emiljimenez21.virtualshop.commands.*;
import me.emiljimenez21.virtualshop.contracts.ItemDB;
import me.emiljimenez21.virtualshop.database.PluginQueries;
import me.emiljimenez21.virtualshop.jobs.*;
import me.emiljimenez21.virtualshop.listeners.PlayerListener;
import me.emiljimenez21.virtualshop.managers.DatabaseManager;
import me.emiljimenez21.virtualshop.managers.ItemManager;
import me.emiljimenez21.virtualshop.managers.JobManager;
import me.emiljimenez21.virtualshop.settings.Messages;
import me.emiljimenez21.virtualshop.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SpigotUpdater;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class Virtualshop extends SimplePlugin {
    private static SpigotUpdater updater;
    private static DatabaseManager db;
    private static ItemManager itemDB = null;
    private static JobManager jobManager = null;
    private static Economy economy = null;

    public int getMetricsPluginId() {
        return 7227;
    }

    @Override
    public SpigotUpdater getUpdateCheck() {
        return new SpigotUpdater(35406);
    }

    @Override
    protected void onPluginStart() {
        jobManager = new JobManager();
        itemDB = new ItemManager();

        updater = new SpigotUpdater(35406);

        if (itemDB.getDB() == null) {
            Common.logFramed(
                    true,
                    "Error Occurred when initializing the item database! Please let the plugin author know!"
            );
            return;
        }

        // Check for vault
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Common.logFramed(
                    true,
                    "You are required to have vault installed to use this plugin!")
            ;
            return;
        }

        // Hook into the economy
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Common.logFramed(
                    true,
                    "Issue finding the Vault Service Provider! Do you have an economy plugin installed?"
            );
            return;
        }

        // Get the economy provider
        economy = rsp.getProvider();

        // Initialize the database after all the checks have been completed
        db = new DatabaseManager();

        // Clear the prefix
        Common.setTellPrefix(null);


        // Register the commands
        Lists.newArrayList(
                new Buy("buy"),
                new Cancel("cancel"),
                new Find("find"),
                new Help("shop"),
                new Sell("sell"),
                new Stock("stock"),
                new Transactions("sales")
        ).forEach(this::registerCommand);

        // Register Event Listeners
        registerEvents(new PlayerListener());

        // Async player load
        jobManager.runAsyncJob(new SyncOnlinePlayers());

        // Remove players hourly
        jobManager.runAsyncRepetitiveJob(new HourlyPlayerCachePurge(), 3600, 3600);
    }

    @Override
    protected void onPluginStop() {
        super.onPluginStop();
        db.getDatabase().close();
    }

    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class, Messages.class);
    }

    public static PluginQueries getDatabase() {
        return db.getDatabase();
    }

    public static ItemDB getItems() {
        return itemDB.getDB();
    }

    public static Economy getEconomy() {
        return economy;
    }

}
