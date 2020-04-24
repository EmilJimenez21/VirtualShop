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
            fromMaterial.put(m, m.toString().toLowerCase());
            fromName.put(m.toString().toLowerCase(), m);
            listNames.add(m.toString().toLowerCase());
        }
    }

    @Override
    public ItemStack get(String name) {
        ItemStack item;
        try {
            item = new ItemStack(fromName.get(name));
        } catch (Exception e) {
            return null;
        }
        return item;
    }

    @Override
    public String get(ItemStack item) {
        String name;
        try {
            name = fromMaterial.get(item.getType());
        } catch (Exception e) {
            return null;
        }
        return name;
    }

    @Override
    public Collection<String> listNames() {
        return listNames;
    }
}
