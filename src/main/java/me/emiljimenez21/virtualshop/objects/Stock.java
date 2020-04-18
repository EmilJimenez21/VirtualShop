package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.managers.PlayerManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
    public int id;
    public String item;
    public ShopPlayer seller;
    public int quantity;
    public Double price;

    // Create stock from info
    public Stock(int id, String item, String seller, int quantity, Double price){
        this.id = id;
        this.item = item;
        this.seller = PlayerManager.getPlayer(seller);
        this.quantity = quantity;
        this.price = price;
    }

    // Create stock from a result set
    public Stock(ResultSet result) throws SQLException {
        this(result.getInt("id"), result.getString("item"), result.getString("seller"), result.getInt("quantity"), result.getDouble("price"));
    }

    public Double calcPrice(int quantity) {
        return price * quantity;
    }

}
