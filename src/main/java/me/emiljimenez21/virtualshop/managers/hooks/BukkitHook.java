package me.emiljimenez21.virtualshop.managers.hooks;

import me.emiljimenez21.virtualshop.contracts.ItemDB;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BukkitHook implements ItemDB {
    private HashMap<String, Material> fromName = new HashMap<>();
    private HashMap<Material, String> fromMaterial = new HashMap<>();
    private List<String> listNames = new ArrayList<>();

    public BukkitHook() {
        for(Material m : Material.values()) {
            fromMaterial.put(m, m.getKey().toString().replace("minecraft:", ""));
            fromName.put(m.getKey().toString().replace("minecraft:", ""), m);
            listNames.add(m.getKey().toString().replace("minecraft:", ""));
        }
    }

    @Override
    public ItemStack get(String name) {
        ItemStack item = null;
        try {
            item = new ItemStack(fromName.get(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public String get(ItemStack item) {
        try {
            return fromMaterial.get(item.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<String> listNames() {
        return listNames;
    }
}
