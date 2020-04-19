package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.objects.ShopPlayer;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Stock extends SimpleCommand {

    public Stock(String label) {
        super(label);
        setPermission("virtualshop.stock");
        setDescription("Retrieve the stock of an item on the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<String>();
        OfflinePlayer player = null;

        if(args.length == 1) {
            for(OfflinePlayer p: Bukkit.getOfflinePlayers()){
                response.add(p.getName());
            }
        } else {
            player = Bukkit.getOfflinePlayer(args[0]);
        }

        if(args.length == 2) {
            List<me.emiljimenez21.virtualshop.objects.Stock> s = Virtualshop.db.retrieveUserStock(player.getUniqueId().toString());
            for(int i = 1; i <= (s.size()/8) + 1; i++){
                response.add(String.valueOf(i));
            }
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
        // Implement a cooldown
        if(!hasPerm("virtualshop.admin")) {
            setCooldown(3, TimeUnit.SECONDS);
        }

        ShopPlayer player;

        if(args.length > 2 || args.length < 1) {
            Messages.send(sender, Messages.HELP_STOCK);
            return;
        }

        try {
            player = PlayerManager.getPlayer(args[0]);
        } catch (Exception e) {
            player = null;
        }

        if(player == null) {
            Messages.send(sender, Messages.ERROR_UNKNOWN_PLAYER.replace("{player}", Messages.formatPlayer(args[0])));
            return;
        }

        List<me.emiljimenez21.virtualshop.objects.Stock> stocks = Virtualshop.db.retrieveUserStock(player.uuid.toString());

        if(stocks.size() == 0) {
            Messages.send(sender, Messages.STOCK_SELLER_NO_STOCK
                .replace("{seller}", Messages.formatPlayer(player.name)));
            return;
        }

        int page = 1;
        if(args.length == 2) {
            // Validate that the second argument is a valid number
            if (!Valid.isInteger(args[1])) {
                Messages.send(sender, Messages.ERROR_BAD_NUMBER);
                return;
            }

            page = Integer.parseInt(args[1]);

            if (page < 0) {
                Messages.send(sender, Messages.ERROR_BAD_NUMBER);
                return;
            }
        }

        int page_size = 8;
        int start = (page - 1) * page_size;
        int pages = stocks.size() / page_size + 1;

        if (page > pages) {
            start = 0;
            page = 1;
        }

        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "STOCK " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + " >> " + Messages.formatPlayer(player.name), '=', ChatColor.DARK_GRAY));
        for(int i = start; i < (start + page_size); i++) {
            try {
                Common.tell(sender, ChatUtil.center(Messages.STOCK_LISTING
                                .replace("{seller}", Messages.formatPlayer(stocks.get(i).seller.name))
                                .replace("{amount}", Messages.formatAmount(stocks.get(i).quantity))
                                .replace("{item}", Messages.formatItem(stocks.get(i).item))
                                .replace("{price}", Messages.formatPrice(stocks.get(i).price))
                        )
                );
            } catch (Exception e){
                break;
            }
        }
        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "PAGE " + ChatColor.YELLOW + page + ChatColor.GRAY + " OF " + ChatColor.YELLOW + pages + ChatColor.DARK_GRAY, '=', ChatColor.DARK_GRAY));
    }
}
