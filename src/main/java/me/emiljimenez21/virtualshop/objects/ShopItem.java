package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.Virtualshop;
import org.bukkit.inventory.ItemStack;

public class ShopItem {
    public ItemStack item;
    public boolean exists;

    public ShopItem(ItemStack item) {
        this.item = item;
        this.exists = true;
    }

    public ShopItem(String item) {
        try {
            this.item = Virtualshop.itemDB.getDB().get(item);
            this.exists = true;
        } catch (Exception e) {
            this.exists = false;
        }
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getName() {
        return Virtualshop.itemDB.getDB().get(this.item);
    }

    public static String getName(ItemStack item) {
        return Virtualshop.itemDB.getDB().get(item);
    }

    public boolean isModified(){
        if(item.hasItemMeta() || item.getEnchantments().size() > 0){
            return true;
        }
        return false;
    }
}
