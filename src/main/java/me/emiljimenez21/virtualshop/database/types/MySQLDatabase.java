package me.emiljimenez21.virtualshop.database.types;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.database.PluginQueries;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDatabase extends PluginQueries {
    private String url;
    private String user;
    private String pass;

    public MySQLDatabase(String host, int port, String user, String password, String database) {
        super();
        this.user = user;
        this.pass = password;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        this.connection = connect();
        init();
    }

    public final Connection connect() {
        Connection cnx = null;
        try {
            cnx = DriverManager.getConnection(url, user, pass);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return cnx;
    }

    @Override
    public void reconnect() {
        connect();
    }

    public void init() {
        try {
            PreparedStatement user_table = prepareStatement("CREATE TABLE IF NOT EXISTS `" + user_tbl + "` (`uuid` VARCHAR(36) NOT NULL, `name` VARCHAR(50) NOT NULL, `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP, INDEX `uuid` (`uuid`), INDEX `name` (`name`))");
            PreparedStatement stock_table = prepareStatement("CREATE TABLE IF NOT EXISTS `" + stock_tbl + "` (`id` INT(11) NOT NULL AUTO_INCREMENT, `item` VARCHAR(50) NOT NULL, `seller` VARCHAR(50) NOT NULL, `quantity` INT(11) NOT NULL, `price` DECIMAL(10,2) NOT NULL, `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP, `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`id`), INDEX `seller` (`seller`), INDEX `item` (`item`))");
            PreparedStatement transaction_table = prepareStatement("CREATE TABLE IF NOT EXISTS `" + transaction_tbl + "` (`id` INT(11) NOT NULL AUTO_INCREMENT, `item` VARCHAR(50) NOT NULL, `seller` VARCHAR(36) NOT NULL, `buyer` VARCHAR(36) NOT NULL, `quantity` INT(11) NOT NULL, `tax` DECIMAL(10,2) NOT NULL, `price` DECIMAL(10,2) NOT NULL, `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`), INDEX `seller` (`seller`), INDEX `item` (`item`), INDEX `buyer` (`buyer`))");

            user_table.execute();
            stock_table.execute();
            transaction_table.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Virtualshop.getInstance());
        }
    }

}
