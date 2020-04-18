package me.emiljimenez21.virtualshop;

import com.earth2me.essentials.Essentials;
import me.emiljimenez21.virtualshop.commands.*;
import me.emiljimenez21.virtualshop.listeners.PlayerListener;
import me.emiljimenez21.virtualshop.managers.MySQLDatabase;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.settings.Messages;
import me.emiljimenez21.virtualshop.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class Virtualshop extends SimplePlugin {
    public static MySQLDatabase db = new MySQLDatabase();
    public static Essentials essentials = null;
    public static Economy economy = null;

    @Override
    protected void onPluginStart() {
        // Initialize the database
        db.init();

        // Load our Dependency
        essentials = JavaPlugin.getPlugin(Essentials.class);
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
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class, Messages.class);
    }
}
