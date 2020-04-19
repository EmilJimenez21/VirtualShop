package me.emiljimenez21.virtualshop.managers.hooks;

import com.Zrips.CMI.CMI;
import me.emiljimenez21.virtualshop.managers.contracts.ItemDB;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;

public class CMIHook implements ItemDB {
    private CMI cmi;
    private Collection<String> items = new ArrayList<>();

    public CMIHook() {
        this.cmi = JavaPlugin.getPlugin(CMI.class);

        // Create the items list
        for(Material m: Material.values()) {
            items.add(cmi.getItemManager().getItem(m).getRealName());
        }
    }

    @Override
    public ItemStack get(String name) {
        return cmi.getItemManager().getItem(name).getItemStack();
    }

    @Override
    public String get(ItemStack item) {
        return cmi.getItemManager().getItem(item).getBukkitName();
    }

    @Override
    public Collection<String> listNames() {
        return items;
    }

}
