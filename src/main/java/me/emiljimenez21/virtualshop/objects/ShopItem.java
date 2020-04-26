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
        ItemStack itm = Virtualshop.getItems().get(item);

        if(itm == null) {
            this.exists = false;
        } else {
            this.item = itm;
            this.exists = true;
        }
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getName() {
        return Virtualshop.getItems().get(this.item);
    }

    public static String getName(ItemStack item) {
        return Virtualshop.getItems().get(item);
    }

    public boolean isModified(){
        if(item.hasItemMeta() || item.getEnchantments().size() > 0){
            return true;
        }
        return false;
    }
}
