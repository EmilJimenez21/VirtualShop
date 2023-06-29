package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.ShopItem;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;


public class Sell extends ShopCommand {

    public Sell(String label) {
        super(label);
        setPermission("virtualshop.sell");
        setDescription("List an item for sale on the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<>();

        if (args.length == 1) {
            response.add("<amount>");
            response.add("all");
        }

        if (args.length == 2) {
            response.addAll(Virtualshop.getItems().listNames());
            response.add("held");
            response.add("hand");
        }

        if (args.length == 3) {
            response.add("<price>");
        }

        return completeLastWord(response);
    }

    @Override
    public void commandLogic() {

        if (args.length != 3) {
            user.playErrorSound();
            sender.sendMessage(Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_SELL
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("<amount>", Messages.formatAmount("<amount>"))
                    .replace("<price>", Messages.formatPrice("<price>"))
            );
            return;
        }

        if (args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("held")) {
            ItemStack hand = getPlayer().getInventory().getItemInMainHand();
            item = new ShopItem(Virtualshop.getItems().get(hand));
        } else {
            if (!loadItem(1)) {
                return;
            }
        }

        if (item.getItem().isSimilar(new ItemStack(Material.AIR))) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_FORBIDDEN_SALE
                    .replace("{item}", Messages.formatItem(item.getName()))
            );
            return;
        }

        if (item.isModified()) {
            user.playErrorSound();
            Messages.send(
                    sender,
                    Messages.ERROR_MODIFIED_ITEM
            );
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            amount = user.getQuantity(item.getItem());
        } else {
            if (!loadAmount(0)) {
                return;
            }
        }

        int available = user.getQuantity(item.getItem());

        if (amount > available) {
            amount = available;
        }

        item.getItem().setAmount(amount);

        if (amount == 0) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_NO_ITEMS
                    .replace("{item}", Messages.formatItem(item.getName()))
            );
            return;
        }

        if (!loadPrice(2)) {
            return;
        }

        Stock stock = new Stock(
                0,
                item.getName(),
                user.uuid.toString(),
                amount,
                price
        );

        Stock dbStock = Virtualshop.getDatabase().createStock(stock);

        if (dbStock != null) {
            user.playPostedListing();
            user.inventory.removeItem(item.getItem());

            Messages.broadcast(Messages.STOCK_BROADCAST
                    .replace(
                            "{seller}",
                            Messages.formatPlayer(getPlayer())
                    )
                    .replace(
                            "{amount}",
                            Messages.formatAmount(dbStock.quantity)
                    )
                    .replace(
                            "{item}",
                            Messages.formatItem(item.getName())
                    )
                    .replace(
                            "{price}",
                            Messages.formatPrice(dbStock.price)
                    )
            );
        }
    }
}
