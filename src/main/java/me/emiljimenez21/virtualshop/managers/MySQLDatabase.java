package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.objects.Transaction;
import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.Bukkit;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase extends SimpleDatabase {
    private String user_tbl;
    private String stock_tbl;
    private String transaction_tbl;

    public void init() {
        user_tbl = Settings.databasePrefix + "user";
        stock_tbl = Settings.databasePrefix + "stock";
        transaction_tbl = Settings.databasePrefix + "transactions";

        // Make the connection to the database
        connect(Settings.databaseHost, Settings.databasePort, Settings.databaseName, Settings.databaseUser, Settings.databasePass);

        // Create the tables if they do not exist
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

    /**
     *
     * User Queries
     *
     */

    public boolean createPlayer(String uuid, String name) {
        PreparedStatement query = null;
        ResultSet result = null;
        try {
            query = this.prepareStatement("INSERT INTO `" + user_tbl + "` (`uuid`, `name`) VALUES (?, ?)");
            query.setString(1, uuid);
            query.setString(2, name);
            return query.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getPlayer(String player) {
        PreparedStatement query = null;
        ResultSet result = null;

        try {
            query = this.prepareStatement("SELECT * FROM `" + user_tbl + "` WHERE name = ? OR uuid = ?");
            query.setString(1, player);
            query.setString(2, player);
            result = query.executeQuery();
            while (result.next()){
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * Stock Queries
     *
     */

    public List<Stock> retrieveItemStock(String item) {
        List<Stock> stocks = new ArrayList<Stock>();
        PreparedStatement query = null;
        ResultSet result = null;
        try {
            query = this.prepareStatement("SELECT * FROM `" + stock_tbl + "` a JOIN users b ON a.seller = b.uuid WHERE item = ? ORDER BY price ASC");
            query.setString(1, item);
            result = query.executeQuery();
            while(result.next()) {
                stocks.add(new Stock(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public List<Stock> retrieveUserStock(String seller) {
        List<Stock> stocks = new ArrayList<Stock>();
        PreparedStatement query = null;
        ResultSet result = null;
        try {
            query = this.prepareStatement("SELECT * FROM `" + stock_tbl + "` WHERE seller = ? ORDER BY quantity DESC");
            query.setString(1, seller);
            result = query.executeQuery();
            while(result.next()) {
                stocks.add(new Stock(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public Stock retrieveStock(Stock stock){
        PreparedStatement query = null;
        ResultSet result = null;

        try {
            query = this.prepareStatement("SELECT * FROM `" + stock_tbl + "` WHERE seller = ? && item = ? ORDER BY quantity DESC");
            query.setString(1, stock.seller.uuid.toString());
            query.setString(2, stock.item);
            result = query.executeQuery();
            while(result.next()) {
                return new Stock(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stock createStock(Stock stock) {
        PreparedStatement query;
        Stock s = retrieveStock(stock);

        if(s == null) {
            try {
                query = this.prepareStatement("INSERT INTO `" + stock_tbl + "` (item, seller, quantity, price) VALUES (?, ?, ?, ?)");
                query.setString(1, stock.item);
                query.setString(2, stock.seller.uuid.toString());
                query.setInt(3, stock.quantity);
                query.setDouble(4, stock.price);
                query.executeUpdate();
                return stock;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            s.quantity += stock.quantity;
            s.price = stock.price;
            updateStock(s);
            return s;
        }
        return null;
    }

    public boolean updateStock(Stock stock) {
        PreparedStatement query = null;
        try {
            query = this.prepareStatement("UPDATE `" + stock_tbl + "` SET quantity = ?, price = ? WHERE id = ?");
            query.setInt(1, stock.quantity);
            query.setDouble(2, stock.price);
            query.setInt(3, stock.id);
            query.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteStock(Stock stock) {
        PreparedStatement query = null;
        try {
            query = this.prepareStatement("DELETE FROM `" + stock_tbl + "` WHERE id = ?");
            query.setInt(1, stock.id);
            query.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * Transaction Queries
     *
     */

    public boolean createTransaction(Transaction tx) {
        PreparedStatement query = null;
        try {
            query = this.prepareStatement("INSERT INTO `" + transaction_tbl + "` (seller, buyer, item, quantity, tax, price) VALUES (?, ?, ?, ?, ?, ?)");
            query.setString(1, tx.seller.uuid.toString());
            query.setString(2, tx.buyer.uuid.toString());
            query.setString(3, tx.item);
            query.setInt(4, tx.quantity);
            query.setDouble(5, tx.tax);
            query.setDouble(6, tx.price);
            query.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Transaction> retrieveUserTransactions(String user) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        PreparedStatement query = null;
        ResultSet result = null;
        try {
            query = this.prepareStatement("SELECT * FROM `" + transaction_tbl + "` WHERE seller = ? OR buyer = ?");
            query.setString(1, user);
            query.setString(2, user);
            result = query.executeQuery();
            while(result.next()) {
                transactions.add(new Transaction(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
