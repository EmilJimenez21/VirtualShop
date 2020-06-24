package me.emiljimenez21.virtualshop;

import me.emiljimenez21.virtualshop.commands.*;
import me.emiljimenez21.virtualshop.contracts.ItemDB;
import me.emiljimenez21.virtualshop.database.PluginQueries;
import me.emiljimenez21.virtualshop.jobs.*;
import me.emiljimenez21.virtualshop.listeners.PlayerListener;
import me.emiljimenez21.virtualshop.managers.AnalyticsManager;
import me.emiljimenez21.virtualshop.managers.DatabaseManager;
import me.emiljimenez21.virtualshop.managers.ItemManager;
import me.emiljimenez21.virtualshop.managers.JobManager;
import me.emiljimenez21.virtualshop.settings.Messages;
import me.emiljimenez21.virtualshop.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class Virtualshop extends SimplePlugin {
    private static Reporting report;
    private static AnalyticsManager analyticsManager;
    private static DatabaseManager db;
    private static ItemManager itemDB = null;
    private static JobManager jobManager = null;
    private static Economy economy = null;
    private static Updater updater = null;

    @Override
    protected void onPluginStart() {
        jobManager = new JobManager();
        analyticsManager = new AnalyticsManager(this);
        report = new Reporting(this);
        report.sendServerData();
        itemDB = new ItemManager();
        updater = new Updater(
                this,
                35406,
                this.getFile(),
                Updater.UpdateType.CHECK_DOWNLOAD,
                true
        );

        if(itemDB.getDB() == null) {
            report.sendError("Issue with loading the item database");
            Common.logFramed(
                    true,
                    "Error Occurred when initializing the item database! Please let the plugin author know!"
            );
            return;
        }

        // Check for vault
        if(getServer().getPluginManager().getPlugin("Vault") == null){
            report.sendError("Server doesn't have vault installed");
            Common.logFramed(
                    true,
                    "You are required to have vault installed to use this plugin!")
            ;
            return;
        }

        // Hook into the economy
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            report.sendError("There is no economy plugin installed");
            Common.logFramed(
                    true,
                    "Issue finding the Vault Service Provider! Do you have an economy plugin installed?"
            );
            return;
        }

        // Initialize the database after all the checks have been completed
        db = new DatabaseManager();

        economy = rsp.getProvider();

        // Register Event Listeners
        registerEvents(new PlayerListener());

        // Register commands
        registerCommand(new Help("shop"));
        registerCommand(new Sell("sell"));
        registerCommand(new Buy( "buy"));
        registerCommand(new Find("find"));
        registerCommand(new Transactions("sales"));
        registerCommand(new Cancel("cancel"));
        registerCommand(new Stock("stock"));

        // Async player load
        jobManager.runAsyncJob(new SyncOnlinePlayers());

        // Check for updates hourly
        jobManager.runAsyncRepetitiveJob(new CheckForUpdates(), 0,3600);

        // Remove players hourly
        jobManager.runAsyncRepetitiveJob(new HourlyPlayerCachePurge(), 3600, 3600);

        // Send plugin stats every 30 mins
        jobManager.runAsyncRepetitiveJob(new ReportMetrics(), 1800, 1800);

        // Send command usage to the plugin developer hourly
        jobManager.runAsyncRepetitiveJob(new SendAnalytics(), 3600, 3600);
    }

    @Override
    protected void onPluginStop() {
        super.onPluginStop();
        db.getDatabase().close();
    }

    @Override
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

    public static Updater getUpdater() {
        return updater;
    }

    public static Reporting getReport() { return report; }

    public static AnalyticsManager getAnalytics() { return  analyticsManager; }
}
