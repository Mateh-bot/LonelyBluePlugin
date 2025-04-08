package org.example.lonelyBluePlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class DamageListener implements Listener {

    private final PlayerLevelManager levelManager;

    public DamageListener(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int level = levelManager.getLevel(player.getUniqueId());
            // Si el jugador tiene nivel negativo (≤ -1) se duplica el daño por fuego, fuego_tick o lava.
            if (level <= -1) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                        event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                        event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setDamage(event.getDamage() * 2);
                }
            }
        }
    }
}
