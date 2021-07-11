package me.emiljimenez21.virtualshop.database.types;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.database.PluginQueries;
import org.bukkit.Bukkit;

import java.sql.*;


public class SQLiteDatabase extends PluginQueries {

    public SQLiteDatabase() {
        try {
             Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + Virtualshop.getInstance().getDataFolder() +  "/shop.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    public void init() {
        try {
            PreparedStatement user_table = prepareStatement("CREATE TABLE IF NOT EXISTS " + user_tbl + " (uuid VARCHAR(50) NOT NULL, name VARCHAR(50) NOT NULL)");
            PreparedStatement stock_table = prepareStatement("CREATE TABLE IF NOT EXISTS " + stock_tbl + " (id INTEGER PRIMARY KEY AUTOINCREMENT, item VARCHAR(50) NOT NULL, seller VARCHAR(50) NOT NULL, quantity INTEGER NOT NULL, price DECIMAL(11,2) NOT NULL)");
            PreparedStatement transaction_table = prepareStatement("CREATE TABLE IF NOT EXISTS " + transaction_tbl + " (id INTEGER PRIMARY KEY AUTOINCREMENT, item VARCHAR(50) NOT NULL, seller VARCHAR(50) NOT NULL, buyer VARCHAR(50) NOT NULL, quantity INTEGER NOT NULL, tax DECIMAL(11,2) NOT NULL, price DECIMAL(11,2) NOT NULL)");

            user_table.execute();
            stock_table.execute();
            transaction_table.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Virtualshop.getInstance());
        }
    }
}
