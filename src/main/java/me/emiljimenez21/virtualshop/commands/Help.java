package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.Virtualshop;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.ChatColor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;



public class Help extends ShopCommand {
    public Help(String label) {
        super(label);
        setPermission("virtualshop.user");
        setDescription("Command reference for the VirtualShop");
    }

    @Override
    protected void onCommand() {
        super.onCommand();
        Virtualshop.getAnalytics().incrementShop();

        Common.tell(sender, ChatUtil.center( ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VirtualShop", '='));

        if(sender.hasPermission("virtualshop.find")) {
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_FIND
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("[page]", Messages.formatAmount("[page]"))
            ));
        }

        if(sender.hasPermission("virtualshop.sell")) {
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_SELL
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("<amount>", Messages.formatAmount("<amount>"))
                    .replace("<price>", Messages.formatPrice("<price>"))
            ));
        }

        if(sender.hasPermission("virtualshop.buy")) {
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_BUY
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("<amount>", Messages.formatAmount("<amount>"))
                    .replace("[price]", Messages.formatPrice("[price]"))
            ));
        }

        if(sender.hasPermission("virtualshop.cancel")) {
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_CANCEL
                    .replace("<item>", Messages.formatItem("<item>"))
                    .replace("[amount]", Messages.formatAmount("[amount]"))
            ));
        }

        if(sender.hasPermission("virtualshop.stock")) {
            if(sender.hasPermission("virtualshop.stock.others")){
                Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_STOCK
                        .replace("<player>", Messages.formatPlayer("<player>"))
                        .replace("[page]", Messages.formatAmount("[page]"))
                ));
            } else {
                Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_STOCK
                        .replace("<player> ", "")
                        .replace("[page]", Messages.formatAmount("[page]"))
                ));
            }
        }

        if (sender.hasPermission("virtualshop.sales")) {
            if (sender.hasPermission("virtualshop.sales.others")) {
                Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_SALES
                        .replace("<player>", Messages.formatPlayer("<player>"))
                        .replace("[page]", Messages.formatAmount("[page]"))
                ));
            } else {
                Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_SALES
                        .replace("<player> ", "")
                        .replace("[page]", Messages.formatAmount("[page]"))
                ));
            }
        }
    }
}
