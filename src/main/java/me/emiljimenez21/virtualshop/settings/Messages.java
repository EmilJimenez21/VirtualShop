package me.emiljimenez21.virtualshop.settings;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.text.DecimalFormat;

public class Messages extends SimpleLocalization {
    public static DecimalFormat formatter = new DecimalFormat("#,##0.00###");
    private static final ChatColor PLAYER_COLOR = ChatColor.AQUA;
    private static final ChatColor QUANTITY_COLOR = ChatColor.YELLOW;
    private static final ChatColor ITEM_COLOR = ChatColor.BLUE;
    private static final ChatColor PRICE_COLOR = ChatColor.DARK_GREEN;
    public static ChatColor BASE_COLOR = ChatColor.RED;

    public static String HELP_BUY, HELP_SELL, HELP_STOCK, HELP_SALES, HELP_CANCEL, HELP_FIND;
    public static String SALES_SALE, SALES_SELF_PURCHASE, SALES_SELLER_SALE, SALES_NO_SALES, SALES_SELF_NO_SALES;
    public static String STOCK_BROADCAST, STOCK_LISTING, STOCK_CANCELLED, STOCK_REMOVED, STOCK_NO_STOCK, STOCK_SELLER_NO_STOCK, STOCK_SELF_NO_STOCK, STOCK_CANCEL_NO_STOCK, STOCK_SELLER_LOWEST_PRICE;
    public static String ERROR_NO_RESULTS, ERROR_FORBIDDEN_SALE, ERROR_MODIFIED_ITEM, ERROR_UNKNOWN_ITEM, ERROR_UNKNOWN_PLAYER, ERROR_BAD_NUMBER, ERROR_BAD_PRICE, ERROR_BAD_ITEM, ERROR_NO_FUNDS, ERROR_NO_ITEMS, ERROR_NO_SPACE;

    @Override
    protected int getConfigVersion() {
        return 6;
    }

    public static void send(CommandSender sender, String message) {
        Common.setTellPrefix("");
        Common.tellNoPrefix(sender, "{prefix}" + Messages.BASE_COLOR + message);
    }

    public static void broadcast(String message) {
        Common.setTellPrefix("");
        Common.broadcast("{prefix}" + Settings.prefix + Messages.BASE_COLOR + message);
    }

    public static void send(org.bukkit.entity.Player player, String message) {
        Common.tell(player, "{prefix}" + Settings.prefix + Messages.BASE_COLOR + message);
    }

    public static String formatPrice(double price) {
        return PRICE_COLOR + "$" + formatter.format(price) + BASE_COLOR;
    }

    public static String formatPrice(String price) {
        return PRICE_COLOR + price + BASE_COLOR;
    }

    public static String formatPlayer(org.bukkit.entity.Player player) {
        return PLAYER_COLOR + player.getName() + BASE_COLOR;
    }

    public static String formatPlayer(String player) {
        return PLAYER_COLOR + player + BASE_COLOR;
    }

    public static String formatAmount(int amount) {
        return QUANTITY_COLOR + String.valueOf(amount) + BASE_COLOR;
    }

    public static String formatAmount(String amount) {
        return QUANTITY_COLOR + amount + BASE_COLOR;
    }

    public static String formatItem(String item) {
        return ITEM_COLOR + item + BASE_COLOR;
    }

    private static void init() {
        pathPrefix("messages.help");
        HELP_BUY = getString("buy");
        HELP_SELL = getString("sell");
        HELP_STOCK = getString("stock");
        HELP_SALES = getString("sales");
        HELP_CANCEL = getString("cancel");
        HELP_FIND = getString("find");

        pathPrefix("messages.sales");
        SALES_SALE = getString("sale");
        SALES_SELF_PURCHASE = getString("self-purchase");
        SALES_SELLER_SALE = getString("seller-sale");
        SALES_NO_SALES = getString("no-sales");
        SALES_SELF_NO_SALES = getString("self-no-sales");

        pathPrefix("messages.stock");
        STOCK_BROADCAST = getString("broadcast");
        STOCK_LISTING = getString("listing");
        STOCK_CANCELLED = getString("cancelled");
        STOCK_REMOVED = getString("removed");
        STOCK_NO_STOCK = getString("no-stock");
        STOCK_SELLER_LOWEST_PRICE = getString("seller-lowest-price");
        STOCK_SELLER_NO_STOCK = getString("seller-no-stock");
        STOCK_SELF_NO_STOCK = getString("self-no-stock");
        STOCK_CANCEL_NO_STOCK = getString("cancel-no-stock");

        pathPrefix("messages.error");
        ERROR_NO_RESULTS = getString("no-results");
        ERROR_FORBIDDEN_SALE = getString("forbidden-item");
        ERROR_MODIFIED_ITEM = getString("modified-item");
        ERROR_UNKNOWN_PLAYER = getString("unknown-player");
        ERROR_UNKNOWN_ITEM = getString("unknown-item");
        ERROR_BAD_NUMBER = getString("bad-number");
        ERROR_BAD_PRICE = getString("bad-price");
        ERROR_BAD_ITEM = getString("bad-item");
        ERROR_NO_FUNDS = getString("no-funds");
        ERROR_NO_ITEMS = getString("no-items");
        ERROR_NO_SPACE = getString("no-space");
    }
}
