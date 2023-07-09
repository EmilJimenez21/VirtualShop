![VirtualShop Logo](https://i.imgur.com/uDhjxhQ.png?1)

# What is VirtualShop?
VirtualShop is a very POWERFUL and LIGHTWEIGHT economy plugin designed to put the value of in-game items in the hands of your players. This is successfully done by creating a pure supply and demand driven economy otherwise known as Capitalism.

# Why VirtualShop?
Are you tired of trying to create your own server shops from scratch? If yes, VirtualShop is your solution! All you have to do is drop the plugin into your plugin folder! BAM, now you have a pure, player run economy at your fingertips and integrated into your server.

# How does this create an economy?
Simple. Players buy and sell items in a global market where they can set prices. If they want to sell items above the market price, they will not make money. It is a constant game of trying to sell the most product at the highest price the market will allow. This plugin is pure Capitalism at its finest.

[![How It Works](https://img.youtube.com/vi/azVxrMIxbJU/0.jpg)](https://www.youtube.com/watch?v=azVxrMIxbJU)


# Features
* Fully Customizable Messages
* Multiple Supported Databases
  * MySQL
  * SQLite (Default)
* Beautiful Chat UI
* Built-in Command Cooldown
* Integrates with ANY vault supported Economy plugin
* Notification sounds on command errors and success
* Tab Completion for commands with support for items, players, and pages

# Requirements
- Java 1.17+
- Vault & Vault supported economy plugin

# Command Reference and Permission Nodes
Argument Reference
<> = Required
[]   = Optional

* /buy \<amount> \<item> [maxprice] - virtualshop.buy - Buys an item.
* /sell \<amount:all> \<item:hand> <price> - virtualshop.sell - Sells an item.
* /find \<item> - virtualshop.find - Finds all listings for the specified item.
* /cancel \<item> [amount] - virtualshop.cancel - Cancels the item you have listed
* /stock \<player> [page] - virtualshop.stock - Finds items that a player has for sale.
* /sales \<player> [page] - virtualshop.sales - Finds all transactions for the specified player.
* /shop - Displays a command reference guide

# Permission Groups
* virtualshop.user
  * virtualshop.buy
  * virtualshop.sell
  * virtualshop.find
  * virtualshop.cancel
  * virtualshop.stock
  * virtualshop.sales
* virtualshop.admin
  * virtualshop.stock.others
  * virtualshop.sales.others


# Frequently Asked Questions

## Help! VirtualShop is not working on my server!
Check to make sure that you have Vault installed as well as a supported economy plugin. VirtualShop is a shop plugin and not economy plugin.

## Does this plugin support NBT items?
No, this plugin is not designed to support NBT items. Players can buy and sell items that are native to Minecraft, that are not renamed, enchanted, or damaged.

# Suggestions or Bug Reports?
Create an Issue on GitHub with either the "Bug" tag or "Feature Request" tag.

# Plugin Metrics
![BStats Plugin Usage](https://bstats.org/signatures/bukkit/VirtualShopPlugin.svg)

### Powered By [Foundation](https://github.com/kangarko/Foundation)
