package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.objects.ShopItem;
import me.emiljimenez21.virtualshop.objects.ShopPlayer;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Cancel extends SimpleCommand {

    public Cancel(String label) {
        super(label);
        setPermission("virtualshop.cancel");
        setDescription("Remove your items from the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<String>();

        if(args.length == 1) {
            response.addAll(Virtualshop.itemDB.getDB().listNames());
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
        // Implement a cooldown
        if(!hasPerm("virtualshop.admin")) {
            setCooldown(3, TimeUnit.SECONDS);
        }

        ShopPlayer player = PlayerManager.getPlayer(getPlayer().getUniqueId().toString());

        if(args.length > 2 || args.length < 1) {
            player.playErrorSound();
            Messages.send(sender, Messages.HELP_CANCEL);
            return;
        }

        // Fetch the available inventory space
        if(player.getAvailableSlots() < 1) {
            player.playErrorSound();
            Messages.send(sender, Messages.ERROR_NO_SPACE);
            return;
        }

        // Fetch your stock
        ShopItem item = new ShopItem(args[0]);

        if(item.getItem() == null) {
            player.playErrorSound();
            Messages.send(sender, Messages.ERROR_UNKNOWN_ITEM
                    .replace("{item}", Messages.formatItem(args[0]))
            );
            return;
        }

        Stock stock = new Stock(0, item.getName(), player.uuid.toString(), 0, 0.00);
        stock = Virtualshop.db.getDatabase().retrieveStock(stock);

        if(stock == null) {
            player.playErrorSound();
            Messages.send(sender, Messages.STOCK_CANCEL_NO_STOCK
                .replace("{item}", Messages.formatItem(item.getName()))
            );
            return;
        }

        // Check to make sure inventory wont overflow
        int cancel_amount = stock.quantity;
        if(args.length == 2 && Valid.isInteger(args[1])) {
            cancel_amount = Integer.valueOf(args[1]);
        }

        if(cancel_amount < 1) {
            player.playErrorSound();
            Messages.send(sender, Messages.ERROR_BAD_NUMBER);
            return;
        }

        if(cancel_amount > (item.getItem().getMaxStackSize() * player.getAvailableSlots())) {
            cancel_amount = item.getItem().getMaxStackSize() * player.getAvailableSlots();
        }

        if(stock.quantity < cancel_amount) {
            cancel_amount = stock.quantity;
        }

        // Update the ItemStack and Stock
        item.getItem().setAmount(cancel_amount);

        if(stock.quantity == cancel_amount) {
            Virtualshop.db.getDatabase().deleteStock(stock);
        } else {
            stock.quantity -= cancel_amount;
            Virtualshop.db.getDatabase().updateStock(stock);
        }

        // Add the items to the players inventory
        player.inventory.addItem(item.getItem());

        // Notify the player how much they took off the market
        Messages.send(sender, Messages.STOCK_CANCELLED
                .replace("{amount}", Messages.formatAmount(cancel_amount))
                .replace("{item}", Messages.formatItem(item.getName()))
        );

        player.playCancelledListing();
    }
}
