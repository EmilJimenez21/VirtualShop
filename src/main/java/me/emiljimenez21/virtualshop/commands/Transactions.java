package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.Transaction;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends ShopCommand {

    public Transactions(String label) {
        super(label);
        setPermission("virtualshop.sales");
        setDescription("Retrieve sales and purchases on the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<>();
        OfflinePlayer player = null;

        if(sender.hasPermission("virtualshop.sales.others")) {
            if (args.length == 1) {
                for (OfflinePlayer user : Bukkit.getOfflinePlayers()) {
                    response.add(user.getName());
                }
            } else {
                player = Bukkit.getOfflinePlayer(args[0]);
            }

            if (args.length == 2) {
                List<Transaction> s = Virtualshop.getDatabase().retrieveUserTransactions(player.getUniqueId().toString());
                for (int i = 1; i <= (s.size() / 8) + 1; i++) {
                    response.add(String.valueOf(i));
                }
            }
        } else {
            if (args.length == 1) {
                player = Bukkit.getOfflinePlayer(sender.getName());
                List<Transaction> s = Virtualshop.getDatabase().retrieveUserTransactions(player.getUniqueId().toString());
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
        Virtualshop.getAnalytics().incrementSales();

        if(sender.hasPermission("virtualshop.sales.others")) {
            if (args.length < 1 || args.length > 2) {
                user.playErrorSound();
                Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + "Command Usage: " + Messages.HELP_SALES
                        .replace("<player>", Messages.formatPlayer("<player>"))
                        .replace("[page]", Messages.formatAmount("[page]"))
                );
                return;
            }

            if (!loadPlayer(0)) {
                return;
            }

            if (args.length == 2) {
                if (!loadPage(1)) {
                    return;
                }
            }
        } else {
            if (args.length > 1) {
                user.playErrorSound();
                Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + "Command Usage: " + Messages.HELP_SALES
                        .replace("<player> ", "")
                        .replace("[page]", Messages.formatAmount("[page]"))
                );
                return;
            }

            player = user;

            if (args.length == 1) {
                if (!loadPage(0)) {
                    return;
                }
            }
        }

        List<Transaction> transactions = Virtualshop.getDatabase().retrieveUserTransactions(player.uuid.toString());

        if(transactions.size() == 0) {
            user.playErrorSound();
            Messages.send(sender, Messages.SALES_NO_SALES
                    .replace("{seller}", Messages.formatPlayer(player.name))
            );
            return;
        }

        int page_size = 8;
        int start = (page - 1) * page_size;
        int pages = transactions.size() / page_size + 1;

        if (page > pages) {
            start = 0;
            page = 1;
        }

        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "TRANSACTIONS " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + " >> " + Messages.formatPlayer(player.name), '='));
        for(int i = start; i < (start + page_size); i++) {
            try {
                String seller = transactions.get(i).seller.name.equalsIgnoreCase(sender.getName()) ? "I" : transactions.get(i).seller.name;
                String buyer = transactions.get(i).buyer.name.equalsIgnoreCase(sender.getName()) ? "me" : transactions.get(i).buyer.name;
                Common.tell(sender, ChatUtil.center(Messages.SALES_SALE
                        .replace("{seller}", Messages.formatPlayer(seller))
                        .replace("{buyer}", Messages.formatPlayer(buyer))
                        .replace("{amount}", Messages.formatAmount(transactions.get(i).quantity))
                        .replace("{item}", Messages.formatItem(transactions.get(i).item))
                        .replace("{price}", Messages.formatPrice(transactions.get(i).price))
                    )
                );
            } catch (Exception e){
                break;
            }
        }
        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "PAGE " + ChatColor.YELLOW + page + ChatColor.GRAY + " OF " + ChatColor.YELLOW + pages + ChatColor.DARK_GRAY, '='));

    }
}
