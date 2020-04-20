package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.objects.ShopItem;
import me.emiljimenez21.virtualshop.objects.ShopPlayer;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Sell extends SimpleCommand {

    public Sell(String label) {
        super(label);
        setPermission("virtualshop.sell");
        setDescription("List an item for sale on the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<String>();

        if(args.length == 1) {
            response.add("<amount>");
            response.add("all");
        }

        if(args.length == 2) {
            response.addAll(Virtualshop.itemDB.getDB().listNames());
            response.add("held");
            response.add("hand");
        }

        if(args.length == 3) {
            response.add("<price>");
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
        ShopItem item;
        ItemStack i;
        int amount;
        double price;

        if(args.length != 3) {
            Messages.send(sender, Messages.HELP_SELL);
            return;
        }

        if(args[1].equalsIgnoreCase("hand") || args[1].equalsIgnoreCase("held")){
            ItemStack hand = getPlayer().getInventory().getItemInMainHand();
            // Have to recreate the item because the getItemInMainHand() function is buggy
            item = new ShopItem(ShopItem.getName(hand));
        } else {
            item = new ShopItem(args[1]);
        }

        i = item.getItem();

        if(i.isSimilar(new ItemStack(Material.AIR))){
            Messages.send(sender, Messages.ERROR_FORBIDDEN_SALE
                    .replace("{item}", Messages.formatItem(item.getName()))
            );
            return;
        }

        if(item.isModified()) {
            Messages.send(sender, Messages.ERROR_MODIFIED_ITEM);
            return;
        }

        int available = player.getQuantity(i);

        if(args[0].equalsIgnoreCase("all")) {
            amount = available;
        } else {
            amount = Integer.parseInt(args[0]) > 0 ? Integer.parseInt(args[0]) : 0;
        }

        if(amount > available){
            amount = available;
        }

        if(amount < 0) {
            Messages.send(sender, Messages.ERROR_BAD_NUMBER);
            return;
        }

        i.setAmount(amount);

        if(amount == 0) {
            Messages.send(sender, Messages.ERROR_NO_ITEMS
                    .replace("{item}", Messages.formatItem(item.getName()))
            );
            return;
        }

        try {
            price = Double.parseDouble(args[2]);
        } catch (Exception e){
            Messages.send(sender, Messages.ERROR_BAD_PRICE);
            return;
        }

        if (price <= 0) {
            Messages.send(sender, Messages.ERROR_BAD_PRICE);
            return;
        }

        Stock stock = new Stock(0, item.getName(), getPlayer().getUniqueId().toString(), amount, price);
        Stock dbStock = Virtualshop.db.getDatabase().createStock(stock);
        if(dbStock != null){
            getPlayer().getInventory().removeItem(i);

            Messages.broadcast(Messages.STOCK_BROADCAST
                    .replace("{seller}", Messages.formatPlayer(getPlayer()))
                    .replace("{amount}", Messages.formatAmount(dbStock.quantity))
                    .replace("{item}", Messages.formatItem(item.getName()))
                    .replace("{price}", Messages.formatPrice(dbStock.price))
            );
        }
    }
}
