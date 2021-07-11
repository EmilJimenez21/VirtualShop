package me.emiljimenez21.virtualshop;
import me.emiljimenez21.virtualshop.commands.*;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
public final class VirtualshopCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerHelpLine(" &6&lPlayer Commands");

        registerSubcommand(new Help("shop"));
        registerSubcommand(new Sell("sell"));
        registerSubcommand(new Buy( "buy"));
        registerSubcommand(new Find("find"));
        registerSubcommand(new Transactions("sales"));
        registerSubcommand(new Cancel("cancel"));
        registerSubcommand(new Stock("stock"));


        registerHelpLine(" ", " &6&lAdmin commands");

        registerSubcommand(new ReloadCommand());

	}
}