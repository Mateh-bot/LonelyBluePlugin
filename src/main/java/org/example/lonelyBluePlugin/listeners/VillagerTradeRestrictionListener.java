package org.example.lonelyBluePlugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class VillagerTradeRestrictionListener implements Listener {

    private final PlayerLevelManager levelManager;

    public VillagerTradeRestrictionListener(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            Player player = event.getPlayer();
            int level = levelManager.getLevel(player.getUniqueId());
            boolean cancel = false;
            String reason = "";

            // Efectos acumulativos:
            // Si nivel <= -1: restringe comercia con Farmer.
            if (level <= -1 && villager.getProfession() == Villager.Profession.FARMER) {
                cancel = true;
                reason = "Trading with Farmer Villagers is disabled at your level.";
            }
            // Si nivel <= -2: restringe comercia con Armorer.
            if (level <= -2 && villager.getProfession() == Villager.Profession.ARMORER) {
                cancel = true;
                reason = "Trading with Armorer Villagers is disabled at your level.";
            }
            // Si nivel <= -3: restringe comercia con Librarian.
            if (level <= -3 && villager.getProfession() == Villager.Profession.LIBRARIAN) {
                cancel = true;
                reason = "Trading with Librarian Villagers is disabled at your level.";
            }

            if (cancel) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + reason);
            }
        }
    }
}
