package me.emiljimenez21.virtualshop.managers;

import com.google.gson.JsonObject;
import org.mineacademy.fo.plugin.SimplePlugin;

public class AnalyticsManager {
    private int buyCommand;
    private int cancelCommand;
    private int findCommand;
    private int shopCommand;
    private int sellCommand;
    private int stockCommand;
    private int salesCommand;

    public AnalyticsManager(SimplePlugin plugin){
        this.reset();
    }

    public void reset() {
        buyCommand = 0;
        cancelCommand = 0;
        findCommand = 0;
        shopCommand = 0;
        sellCommand = 0;
        stockCommand = 0;
        salesCommand = 0;
    }

    public void incrementBuy() {
        buyCommand++;
    }

    public void incrementCancel() {
        cancelCommand++;
    }

    public void incrementFind() {
        findCommand++;
    }

    public void incrementShop() {
        shopCommand++;
    }

    public void incrementSell() {
        sellCommand++;
    }

    public void incrementStock() {
        stockCommand++;
    }

    public void incrementSales() {
        salesCommand++;
    }

    public JsonObject getData() {
        JsonObject data = new JsonObject();
        data.addProperty("buy_usage", buyCommand);
        data.addProperty("cancel_usage", cancelCommand);
        data.addProperty("find_usage", findCommand);
        data.addProperty("shop_usage", shopCommand);
        data.addProperty("sell_usage", sellCommand);
        data.addProperty("stock_usage", stockCommand);
        data.addProperty("sales_usage", salesCommand);
        return data;
    }
}
