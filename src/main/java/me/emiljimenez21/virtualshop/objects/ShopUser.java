package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopUser {
    public UUID uuid = null;
    public String name = null;
    public Inventory inventory = null;
    public Player player = null;

    /**
     * Create a shop user by a string UUID and a name
     *
     * @param uuid The UUID of a player
     * @param name The Name of a player
     */
    public ShopUser(String uuid, String name) {
        this(UUID.fromString(uuid), name);
    }

    /**
     * Create a shop user using a player
     *
     * @param player The player that this user will represent
     */
    public ShopUser(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.inventory = player.getInventory();
    }


    /**
     * Create a shop user by a UUID and their Name
     * @param uuid The UUID of the player
     * @param name The name of the player associated with the UUID
     */
    public ShopUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.player = Bukkit.getPlayer(uuid);
        if (this.player == null) {
            this.player = Bukkit.getOfflinePlayer(uuid).getPlayer();
        }

        if(this.player != null) {
            this.inventory = player.getInventory();
        }
    }

    private void playSound(Sound sound) {
        if(Settings.sound) {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            } catch (Exception e) {
                // Do nothing
            }
        }
    }



    public void playErrorSound() {
        this.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
        this.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
    }

    public void playPostedListing() {
        this.playSound(Sound.BLOCK_NOTE_BLOCK_BELL);
    }

    public void playCancelledListing() {
        this.playSound(Sound.BLOCK_NOTE_BLOCK_HARP);
    }

    public void playPurchased() {
        this.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME);
    }

    public void playProductSold() {
        this.playSound(Sound.ENTITY_PLAYER_LEVELUP);
    }

    public int getAvailableSlots() {
        int quantity = 0;

        for(int i = 0; i < this.inventory.getSize()-5; i++) {
            ItemStack slot = this.inventory.getItem(i);

            if (slot == null) {
                quantity++;
            }
        }
        return quantity;
    }

    public int getQuantity(ItemStack lookup){
        int quantity = 0;

        for(int i = 0; i < this.inventory.getSize(); i++) {
            ItemStack slot = this.inventory.getItem(i);

            if(slot == null) {
                continue;
            }

            ShopItem sItem = new ShopItem(slot);

            if(!sItem.isModified()) {
                if(slot.getType() == lookup.getType()) {
                    quantity += slot.getAmount();
                }
            }
        }

        return quantity;
    }
}
