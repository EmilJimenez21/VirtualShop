package me.emiljimenez21.virtualshop.objects;

import me.emiljimenez21.virtualshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopPlayer {
    public UUID uuid = null;
    public String name = null;
    public Inventory inventory = null;
    public Player player = null;

    public ShopPlayer(String uuid, String name) {
        this(UUID.fromString(uuid), name);
    }

    public ShopPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.inventory = player.getInventory();
    }

    public ShopPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        loadOnlinePlayer();
    }

    public void playErrorSound() {
        if(Settings.sound)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    }

    public void playPostedListing() {
        if(Settings.sound)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

    public void playCancelledListing() {
        if(Settings.sound)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
    }

    public void playPurchased() {
        if(Settings.sound)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
    }

    public void playProductSold() {
        if(Settings.sound)
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    private void loadOnlinePlayer() {
        this.player = Bukkit.getPlayer(uuid);
        if (this.player != null) {
            this.inventory = player.getInventory();
        }
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
