package me.emiljimenez21.virtualshop.managers;

import me.emiljimenez21.virtualshop.contracts.ItemDB;
import me.emiljimenez21.virtualshop.managers.hooks.CMIHook;
import me.emiljimenez21.virtualshop.managers.hooks.EssentialsHook;
import org.mineacademy.fo.Common;

public class ItemManager {
    private ItemDB db = null;

    public ItemManager() {
        if(Common.doesPluginExist("Essentials")){
            db = new EssentialsHook();
        }

        if(Common.doesPluginExist("CMI")) {
            db = new CMIHook();
        }
    }

    public ItemDB getDB() {
        return this.db;
    }

}
