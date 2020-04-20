package me.emiljimenez21.virtualshop;

import me.emiljimenez21.virtualshop.commands.*;
import me.emiljimenez21.virtualshop.listeners.PlayerListener;
import me.emiljimenez21.virtualshop.managers.DatabaseManager;
import me.emiljimenez21.virtualshop.managers.ItemManager;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.settings.Messages;
import me.emiljimenez21.virtualshop.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class Virtualshop extends SimplePlugin {
    public static DatabaseManager db;
    public static ItemManager itemDB = null;
    public static Economy economy = null;

    @Override
    protected void onPluginStart() {
        // Initialize the database
        db = new DatabaseManager();

        // Load our ItemManager
        itemDB = new ItemManager();

        // Disable Plugin if no applicable itemDB Plugins exist
        if(itemDB.getDB() == null) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Hook into the economy
        economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

        // Load all online players into the cache
        for(Player p: Bukkit.getOnlinePlayers()){
            PlayerManager.addPlayer(p);
        }

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
}
