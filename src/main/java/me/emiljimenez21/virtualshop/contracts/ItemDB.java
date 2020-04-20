package me.emiljimenez21.virtualshop.contracts;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface ItemDB {
    ItemStack get(String name);
    String get(ItemStack item);
    Collection<String> listNames();
}
