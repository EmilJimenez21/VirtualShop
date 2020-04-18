package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.objects.ShopPlayer;
import me.emiljimenez21.virtualshop.objects.Transaction;
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

public class Transactions extends SimpleCommand {

    public Transactions(String label) {
        super(label);
        setPermission("virtualshop.transactions");
        setDescription("Retrieve sales and purchases on the VirtualShop");
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
            List<Transaction> s = Virtualshop.db.retrieveUserTransactions(player.getUniqueId().toString());
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

        if(args.length < 1 || args.length > 2) {
            Messages.send(sender, Messages.HELP_SALES);
            return;
        }

        player = PlayerManager.getPlayer(args[0]);

        if(player == null) {
            Messages.send(sender, Messages.ERROR_UNKNOWN_PLAYER
                    .replace("{player}", Messages.formatPlayer(args[0]))
            );
            return;
        }

        int page = 1;
        if(args.length == 2) {
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

        List<Transaction> transactions = Virtualshop.db.retrieveUserTransactions(player.uuid.toString());

        if(transactions.size() == 0) {
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

        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "TRANSACTIONS " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + " >> " + Messages.formatPlayer(player.name), '=', ChatColor.DARK_GRAY));
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
        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "PAGE " + ChatColor.YELLOW + page + ChatColor.GRAY + " OF " + ChatColor.YELLOW + pages + ChatColor.DARK_GRAY, '=', ChatColor.DARK_GRAY));

    }
}
