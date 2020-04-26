package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class Cancel extends ShopCommand {

    public Cancel(String label) {
        super(label);
        setPermission("virtualshop.cancel");
        setDescription("Remove your items from the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<String>();

        if(args.length == 1) {
            response.addAll(Virtualshop.getItems().listNames());
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
        super.onCommand();

        if(args.length > 2 || args.length < 1) {
            user.playErrorSound();
            Common.tell(sender,Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_CANCEL
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("[amount]", Messages.formatAmount("[amount]"))
            );
            return;
        }

        // Fetch the available inventory space
        if(user.getAvailableSlots() < 1) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_NO_SPACE);
            return;
        }

        if(!loadItem(0)) {
            return;
        }

        Stock stock = new Stock(item,user);

        stock = Virtualshop.getDatabase().retrieveStock(stock);

        if(stock == null) {
            user.playErrorSound();
            Messages.send(
                    sender,
                    Messages.STOCK_CANCEL_NO_STOCK
                            .replace(
                                    "{item}",
                                    Messages.formatItem(
                                            item.getName()
                                    )
                            )
            );
            return;
        }

        // Check to make sure inventory wont overflow
        int maximum_space = item.getItem().getMaxStackSize() * user.getAvailableSlots();

        amount = stock.quantity;
        if(args.length == 2){
            if(!loadAmount(1)){
                return;
            }
        }

        // Cancel up to the maximum inventory space
        if(amount > maximum_space) {
            amount = maximum_space;
        }

        // Cancel up to the amount that the stock has
        if(stock.quantity < amount) {
            amount = stock.quantity;
        }

        // Update the ItemStack and Stock
        item.getItem().setAmount(amount);

        if(stock.quantity == amount) {
            Virtualshop.getDatabase().deleteStock(stock);
        } else {
            stock.quantity -= amount;
            Virtualshop.getDatabase().updateStock(stock);
        }

        // Add the items to the users inventory
        user.inventory.addItem(item.getItem());

        // Notify the user how much they took off the market
        Messages.send(
                sender,
                Messages.STOCK_CANCELLED
                        .replace(
                                "{amount}",
                                Messages.formatAmount(amount)
                        )
                        .replace(
                                "{item}",
                                Messages.formatItem(item.getName())
                        )
        );

        user.playCancelledListing();
    }
}
