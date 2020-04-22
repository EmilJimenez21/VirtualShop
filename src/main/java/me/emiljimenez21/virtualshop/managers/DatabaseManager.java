package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.database.types.MySQLDatabase;
import me.emiljimenez21.virtualshop.database.PluginQueries;
import me.emiljimenez21.virtualshop.database.types.SQLiteDatabase;
import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;

public class DatabaseManager {
    private PluginQueries database;

    public DatabaseManager() {
        if(Settings.databaseEnabled) {
            database = new MySQLDatabase(
                    Settings.databaseHost,
                    Settings.databasePort,
                    Settings.databaseUser,
                    Settings.databasePass,
                    Settings.databaseName
            );
            Common.log("Using MySQL");
        } else {
            database = new SQLiteDatabase();
            Common.log("Using SQLite");
        }

        if(!database.isLoaded()) {
            if(database instanceof MySQLDatabase) {
                Common.logFramed("Unable to connect to MySQL... Using SQLite for now...");
                database = new SQLiteDatabase();
            } else {
                Bukkit.getPluginManager().disablePlugin(Virtualshop.getInstance());
            }
        }
    }

    public PluginQueries getDatabase() {
        return database;
    }

}
