package me.emiljimenez21.virtualshop.commands;

import me.emiljimenez21.virtualshop.managers.PlayerManager;
import me.emiljimenez21.virtualshop.objects.ShopItem;
import me.emiljimenez21.virtualshop.objects.ShopUser;
import me.emiljimenez21.virtualshop.settings.Messages;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.concurrent.TimeUnit;

public abstract class ShopCommand extends SimpleCommand {
    protected ShopUser user;
    protected ShopItem item = null;
    protected Integer amount = null;
    protected Integer page = 1;
    protected Double price = null;
    protected ShopUser player = null;

    protected ShopCommand(String label) {
        super(label);
    }

    @Override
    protected void onCommand() {
        user = PlayerManager.getPlayer(getPlayer().getUniqueId().toString());

        if (!hasPerm("virtualshop.admin")) {
            setCooldown(3, TimeUnit.SECONDS);
        }

        commandLogic();

        item = null;
        amount = null;
        page = 1;
        price = null;
        player = null;
    }

    public abstract void commandLogic();

    protected boolean loadItem(Integer ARG_ITEM) {
        item = new ShopItem(args[ARG_ITEM]);
        if (!item.exists) {
            user.playErrorSound();
            Messages.send(
                    sender,
                    Messages.BASE_COLOR + Messages.ERROR_UNKNOWN_ITEM
                            .replace(
                                    "{item}",
                                    Messages.formatItem(args[ARG_ITEM])
                            )
            );
            return false;
        }
        return true;
    }

    protected boolean loadPlayer(Integer ARG_PLAYER) {
        try {
            player = PlayerManager.getPlayer(args[ARG_PLAYER]);
            if (player == null) {
                throw new Exception("That player does not exist");
            }
        } catch (Exception e) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_UNKNOWN_PLAYER
                    .replace(
                            "{player}",
                            Messages.formatPlayer(args[ARG_PLAYER])
                    )
            );
            return false;
        }
        return true;
    }

    protected boolean loadPage(Integer ARG_PAGE) {
        try {
            page = Integer.parseInt(args[ARG_PAGE]);
            if (page < 0) {
                throw new Exception("The page number must be greater than 0");
            }
        } catch (Exception e) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_BAD_NUMBER);
            return false;
        }
        return true;
    }

    protected boolean loadPrice(Integer ARG_PRICE) {
        try {
            price = Double.parseDouble(args[ARG_PRICE]);
            if (price < 0.001) {
                throw new Exception("Price needs to be greater than 0.00");
            }
        } catch (Exception e) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_BAD_PRICE);
            return false;
        }
        return true;
    }

    protected boolean loadAmount(Integer ARG_AMOUNT) {
        try {
            amount = Integer.parseInt(args[ARG_AMOUNT]);
            if (amount < 1) {
                throw new Exception("Amount needs to be a positive number");
            }
        } catch (Exception e) {
            user.playErrorSound();
            Messages.send(sender, Messages.ERROR_BAD_NUMBER);
            return false;
        }
        return true;
    }

}
