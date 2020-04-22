package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.objects.Stock;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.ChatColor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;


public class Find extends ShopCommand {

    public Find(String label) {
        super(label);
        setPermission("virtualshop.find");
        setDescription("Find an item from the VirtualShop");
    }

    @Override
    protected List<String> tabComplete() {
        List<String> response = new ArrayList<>();

        if(args.length == 1) {
            response.addAll(Virtualshop.itemDB.getDB().listNames());
        }

        if(args.length == 2) {
            response.add("[page]");
        }

        return completeLastWord(response);
    }

    @Override
    protected void onCommand() {
        super.onCommand();

        // Blueprint for arguments
        if(args.length < 1 || args.length > 2) {
            user.playErrorSound();
            Common.tell(sender, Messages.BASE_COLOR + "Command Usage: " + Messages.HELP_FIND
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("[page]", Messages.formatAmount("[page]"))
            );
            return;
        }

        if(!loadItem(0)){
            return;
        }

        if(args.length == 2) {
            if(!loadPage(1)){
                return;
            }
        }

        List<Stock> stocks = Virtualshop.db.getDatabase().retrieveItemStock(item.getName());

        if(stocks.size() == 0) {
            user.playErrorSound();
            Messages.send(sender, Messages.STOCK_NO_STOCK
                .replace("{item}", Messages.formatItem(item.getName())));
            return;
        }

        int page_size = 8;
        int start = (page - 1) * page_size;
        int pages = stocks.size() / page_size + 1;

        if (page > pages) {
            start = 0;
            page = 1;
        }

        Common.tell(sender, ChatUtil.center( ChatColor.GRAY + "STOCK " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + " >> " + Messages.formatItem(item.getName().toUpperCase() + ChatColor.DARK_GRAY), '=', ChatColor.DARK_GRAY));
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
