package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.settings.Messages;
import org.bukkit.ChatColor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;



public class Help extends ShopCommand {
    public Help(String label) {
        super(label);
        setDescription("Command reference for the VirtualShop");
    }

    @Override
    protected void onCommand() {
        super.onCommand();

        Common.tell(sender, ChatUtil.center( ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VirtualShop", '=', ChatColor.DARK_GRAY));

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
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_STOCK
                    .replace("<player>", Messages.formatPlayer("<player>"))
                    .replace("[page]", Messages.formatAmount("[page]"))
            ));
        }

        if (sender.hasPermission("virtualshop.sales")) {
            Common.tell(sender, ChatUtil.center(Messages.BASE_COLOR + Messages.HELP_SALES
                    .replace("<player>", Messages.formatPlayer("<player>"))
                    .replace("[page]", Messages.formatAmount("[page]"))
            ));
        }
    }
}
