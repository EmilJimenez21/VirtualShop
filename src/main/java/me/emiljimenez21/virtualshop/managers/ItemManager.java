package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.contracts.ItemDB;
import me.emiljimenez21.virtualshop.managers.hooks.BukkitHook;

public class ItemManager {
    private ItemDB db = null;

    public ItemManager() {
        db = new BukkitHook();
    }

    public ItemDB getDB() {
        return this.db;
    }

}
