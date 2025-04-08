package org.example.lonelyBluePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class ItemUseListener implements Listener {

    private final PlayerLevelManager levelManager;

    public ItemUseListener(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String display = item.getItemMeta().getDisplayName();
            if (display.equals("Level Upgrader")) {
                event.setCancelled(true);
                if (levelManager.getLevel(player.getUniqueId()) < 0) {
                    boolean upgraded = levelManager.upgradeNegativeLevel(player.getUniqueId());
                    if (upgraded) {
                        player.sendMessage(ChatColor.GREEN + "Your negative level has been upgraded by 1!");
                        // Consume one Level Upgrader item
                        int amount = item.getAmount();
                        if (amount > 1) {
                            item.setAmount(amount - 1);
                        } else {
                            player.getInventory().removeItem(item);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not eligible to upgrade your level.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Level Upgrader only works when your level is negative.");
                }
            }

            // Revive Token handling (placeholder)
            if (display.equals("Revive Token")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.GREEN + "Revive Token activated. (Functionality not implemented)");
                int amount = item.getAmount();
                if (amount > 1) {
                    item.setAmount(amount - 1);
                } else {
                    player.getInventory().removeItem(item);
                }
            }
        }
    }
}
