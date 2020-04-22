package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.managers.PlayerManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {
    public int id;
    public String item;
    public ShopUser seller;
    public ShopUser buyer;
    public int quantity;
    public double tax;
    public double price;


    public Transaction(int id, String item, String seller, String buyer, int quantity, double tax, double price)
    {
        this.id = id;
        this.item = item;
        this.seller = PlayerManager.getPlayer(seller);
        this.buyer = PlayerManager.getPlayer(buyer);
        this.quantity = quantity;
        this.tax = tax;
        this.price = price;
    }

    public Transaction(String item, String seller, String buyer, int quantity, double tax, double price)
    {
        this(
            0,
            item,
            seller,
            buyer,
            quantity,
            tax,
            price
        );

    }


    public Transaction(ResultSet result) throws SQLException {
        this (
            result.getInt("id"),
            result.getString("item"),
            result.getString("seller"),
            result.getString("buyer"),
            result.getInt("quantity"),
            result.getDouble("tax"),
            result.getDouble("price")
        );
    }
}
