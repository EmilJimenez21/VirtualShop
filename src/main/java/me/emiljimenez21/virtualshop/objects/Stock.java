package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.managers.PlayerManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock {
    public int id;
    public String item;
    public ShopUser seller;
    public int quantity;
    public Double price;

    /**
     *
     *
     * @param id The ID of the stock listng from the DB
     * @param item The name of the item in the listing
     * @param seller The uuid of the seller
     * @param quantity The amount of the item the player has in the market
     * @param price The price per each of the item
     */
    public Stock(int id, String item, String seller, int quantity, Double price){
        this.id = id;
        this.item = item;
        this.seller = PlayerManager.getPlayer(seller);
        this.quantity = quantity;
        this.price = price;
    }

    public Stock(ShopItem item, ShopUser user){
        this.item = item.getName();
        this.seller = user;
    }

    /**
     * Create a stock object from the database resultset
     *
     * @param result Stock Resultset
     * @throws SQLException Error for when a stock field isn't present
     */
    public Stock(ResultSet result) throws SQLException {
        this (
                result.getInt("id"),
                result.getString("item"),
                result.getString("seller"),
                result.getInt("quantity"),
                result.getDouble("price")
        );
    }

    /**
     * Helper function to calculate the price based on the desired purchase quantity
     *
     * @param quantity The desired quantity to be purchased
     * @return
     */
    public Double calcPrice(int quantity) {
        return price * quantity;
    }

}
