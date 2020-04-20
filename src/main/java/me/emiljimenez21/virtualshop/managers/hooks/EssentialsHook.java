package me.emiljimenez21.virtualshop.managers.hooks;

import com.earth2me.essentials.Essentials;
import me.emiljimenez21.virtualshop.contracts.ItemDB;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class EssentialsHook implements ItemDB {
    private Essentials ess;

    public EssentialsHook(){
        ess = JavaPlugin.getPlugin(Essentials.class);
    }

    @Override
    public ItemStack get(String name) {
        try {
            return ess.getItemDb().get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<String> listNames() {
        return ess.getItemDb().listNames();
    }

    public String get(ItemStack item) {
        return ess.getItemDb().name(item);
    }
}
