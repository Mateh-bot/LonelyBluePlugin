package org.example.lonelyBluePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class EnchantingTableRestrictionListener implements Listener {

    private final PlayerLevelManager levelManager;

    public EnchantingTableRestrictionListener(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Verifica si se intenta abrir una mesa de encantamientos.
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                int level = levelManager.getLevel(player.getUniqueId());
                if (level <= -2) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot use enchanting tables at your level!");
                }
            }
        }
    }
}
