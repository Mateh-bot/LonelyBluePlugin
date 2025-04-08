package org.example.lonelyBluePlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.example.lonelyBluePlugin.managers.AbilityManager;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class PlayerDeathListener implements Listener {

    private final PlayerLevelManager levelManager;
    private final AbilityManager abilityManager;

    public PlayerDeathListener(PlayerLevelManager levelManager, AbilityManager abilityManager) {
        this.levelManager = levelManager;
        this.abilityManager = abilityManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage != null && lastDamage.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (lastDamage instanceof org.bukkit.event.entity.EntityDamageByEntityEvent damageEvent) {
                if (damageEvent.getDamager() instanceof Player killer) {
                    levelManager.increaseLevel(killer.getUniqueId());
                    levelManager.decreaseLevel(victim.getUniqueId());
                    killer.sendMessage("Your level has increased to " + levelManager.getLevel(killer.getUniqueId()));
                    victim.sendMessage("Your level has decreased to " + levelManager.getLevel(victim.getUniqueId()));
                }
            }
        }
        // Si la muerte no fue por un jugador, no se altera el nivel.
    }
}
