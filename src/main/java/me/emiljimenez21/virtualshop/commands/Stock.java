package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class Stock extends ShopCommand {

    public Stock(String label) {
        super(label);
        setPermission("virtualshop.stock");
        setDescription("View your stock on the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<>();
        OfflinePlayer player = null;

        if(sender.hasPermission("virtualshop.stock.others")) {
            if (args.length == 1) {
                for (OfflinePlayer user : Bukkit.getOfflinePlayers()) {
                    response.add(user.getName());
                }
            } else {
                player = Bukkit.getOfflinePlayer(args[0]);
            }

            if (args.length == 2) {
                List<me.emiljimenez21.virtualshop.objects.Stock> s = Virtualshop.getDatabase().retrieveUserStock(player.getUniqueId().toString());
                for (int i = 1; i <= (s.size() / 8) + 1; i++) {
                    response.add(String.valueOf(i));
                }
            }
        } else {
            if (args.length == 1) {
                player = Bukkit.getOfflinePlayer(sender.getName());
                List<me.emiljimenez21.virtualshop.objects.Stock> s = Virtualshop.getDatabase().retrieveUserStock(player.getUniqueId().toString());
                for (int i = 1; i <= (s.size() / 8) + 1; i++) {
                    response.add(String.valueOf(i));
                }
            }
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
        super.onCommand();

        Virtualshop.getAnalytics().incrementStock();

        if(sender.hasPermission("virtualshop.stock.others")) {
            if (args.length > 2 || args.length < 1) {
                user.playErrorSound();
                Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_STOCK
                        .replace("<player>", Messages.formatPlayer("<player>"))
                        .replace("[page]", Messages.formatAmount("[page]"))
                );
                return;
            }

            if (!loadPlayer(0)) {
                return;
            }

            if(args.length == 2) {
                if(!loadPage(1)){
                    return;
                }
            }
        } else {
            if(args.length > 1){
                user.playErrorSound();
                Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_STOCK
                        .replace("<player> ", "")
                        .replace("[page]", Messages.formatAmount("[page]"))
                );
                return;
            }
            // Set the player to the current user
            player = user;

            if(args.length == 1) {
                if(!loadPage(0)){
                    return;
                }
            }
        }

        List<me.emiljimenez21.virtualshop.objects.Stock> stocks = Virtualshop.getDatabase().retrieveUserStock(player.uuid.toString());

        if(stocks.size() == 0) {
            user.playErrorSound();
            Messages.send(sender, Messages.STOCK_SELLER_NO_STOCK
                .replace("{seller}", Messages.formatPlayer(player.name)));
            return;
        }

        int page_size = 8;
        int start = (page - 1) * page_size;
        int pages = stocks.size() / page_size + 1;

        if (page > pages) {
            start = 0;
            page = 1;
        }

        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "STOCK " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + " >> " + Messages.formatPlayer(player.name), '='));
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
        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "PAGE " + ChatColor.YELLOW + page + ChatColor.GRAY + " OF " + ChatColor.YELLOW + pages + ChatColor.DARK_GRAY, '='));

    }
}
