package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.objects.Transaction;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;

import java.util.ArrayList;
import java.util.List;

public class Buy extends ShopCommand {

    public Buy(String label) {
        super(label);
        setPermission("virtualshop.buy");
        setDescription("Buy an item from the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<>();

        if(args.length == 1) {
            response.addAll(Virtualshop.getItems().listNames());
            response.add("held");
            response.add("hand");
        }

        if(args.length == 2) {
            response.add("<amount>");
        }

        if(args.length == 3) {
            response.add("[price]");
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
       super.onCommand();

        if(args.length < 2 || args.length > 3) {
            user.playErrorSound();
            Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_BUY
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("<amount>", Messages.formatAmount("<amount>"))
                    .replace("[price]", Messages.formatPrice("[price]"))
            );
            return;
        }

        if(!loadItem(0)){
            return;
        }

        if(!loadAmount(1)){
            return;
        }

        if(args.length == 3){
            if(!loadPrice(2)){
                return;
            }
        }

        int available_space = user.getAvailableSlots();
        int max_amount = available_space * item.getItem().getMaxStackSize();

        if(max_amount < amount) {
            amount = max_amount;
        }

        if(available_space == 0) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_NO_SPACE);
            return;
        }

        // Retrieve the stocks from the DB
        List<Stock> stocks = Virtualshop.getDatabase().retrieveItemStock(item.getName());

        int purchased_amount = 0;
        double total_price = 0;

        for(Stock stock: stocks) {
            int purchase_amount;

            // Stop buying if i've purchased all I want
            if(purchased_amount == amount) {
                break;
            }

            // Stop buying if I am the seller
            if(stock.seller.uuid.equals(getPlayer().getUniqueId())){
                // Notify the player they are the cheapest on the market
                Messages.send(
                        getPlayer(),
                        Messages.STOCK_SELLER_LOWEST_PRICE
                                .replace(
                                        "{item}",
                                        Messages.formatItem(item.getName())
                                )
                );
                break;
            }

            // Stop buying if it's too expensive
            if(price != null){
                if(stock.price > price) {
                    break;
                }
            }

            int remaining_amount = amount - purchased_amount;

            // Handle stocks that might be less than what I want to buy
            if(stock.quantity < remaining_amount) {
                purchase_amount = stock.quantity;
            } else {
                purchase_amount = remaining_amount;
            }

            double purchase_price = stock.calcPrice(purchase_amount);

            // Check to see if the user can purchase this amount
            if(HookManager.getBalance(getPlayer()) < purchase_price){
                user.playErrorSound();
                Messages.send(sender, Messages.ERROR_NO_FUNDS);
                return;
            }

            // Update the amount being purchased
            item.getItem().setAmount(purchase_amount);

            // Create the transaction
            Transaction tx = new Transaction(
                    stock.item,
                    stock.seller.uuid.toString(),
                    user.uuid.toString(),
                    purchase_amount,
                    0,
                    stock.price
            );

            // Create a transaction record
            if(Virtualshop.getDatabase().createTransaction(tx)){
                total_price += purchase_price;
                purchased_amount += purchase_amount;

                // Take the money from the user
                Virtualshop.getEconomy().withdrawPlayer(getPlayer(), purchase_price);

                // Give the money to the seller
                Virtualshop.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(stock.seller.uuid), purchase_price);

                // Update the stock
                if(stock.quantity == purchase_amount) {
                    Virtualshop.getDatabase().deleteStock(stock);
                } else {
                    stock.quantity -= purchase_amount;
                    Virtualshop.getDatabase().updateStock(stock);
                }

                // Add the items to the users inventory
                getPlayer().getInventory().addItem(item.getItem());

                // Tell the seller how much they sold
                if(stock.seller.player != null){
                    stock.seller.playProductSold();
                    Messages.send((CommandSender) stock.seller.player,
                            Messages.SALES_SELLER_SALE
                                    .replace("{buyer}", Messages.formatPlayer(getPlayer()))
                                    .replace("{amount}", Messages.formatAmount(purchase_amount))
                                    .replace("{item}", Messages.formatItem(item.getName()))
                                    .replace("{price}", Messages.formatPrice(purchase_price))
                    );
                }
            }
        }

        if(purchased_amount == 0){
            user.playErrorSound();
            Messages.send(sender, Messages.STOCK_NO_STOCK
                    .replace("{item}", Messages.formatItem(item.getName()))
            );
        } else {
            user.playPurchased();
            Messages.send(sender, Messages.SALES_SELF_PURCHASE
                    .replace("{amount}", Messages.formatAmount(purchased_amount))
                    .replace("{item}", Messages.formatItem(item.getName()))
                    .replace("{price}", Messages.formatPrice(total_price))
            );
        }
    }
}
