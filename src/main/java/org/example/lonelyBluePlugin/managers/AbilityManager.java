package org.example.lonelyBluePlugin.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AbilityManager {

    private final PlayerLevelManager levelManager;

    // Cooldown maps for each positive ability level.
    private final HashMap<UUID, Long> cooldownLevel1 = new HashMap<>();
    private final HashMap<UUID, Long> cooldownLevel2 = new HashMap<>();
    private final HashMap<UUID, Long> cooldownLevel3 = new HashMap<>();

    // Durations in milliseconds.
    private final long COOLDOWN_LEVEL1 = 60 * 1000;     // 1 minute
    private final long COOLDOWN_LEVEL2 = 60 * 1000;     // 1 minute
    private final long COOLDOWN_LEVEL3 = 3 * 60 * 1000; // 3 minutes

    public AbilityManager(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void triggerAbility(Player activator) {
        int level = levelManager.getLevel(activator.getUniqueId());
        if (level > 0) {
            switch(level) {
                case 1:
                    if (canUseAbility(activator, 1)) {
                        abilityLevel1(activator);
                        cooldownLevel1.put(activator.getUniqueId(), System.currentTimeMillis());
                    } else {
                        long remaining = COOLDOWN_LEVEL1 - (System.currentTimeMillis() - cooldownLevel1.get(activator.getUniqueId()));
                        activator.sendMessage("Ability +1 is on cooldown for " + (remaining / 1000) + " seconds.");
                    }
                    break;
                case 2:
                    if (canUseAbility(activator, 2)) {
                        abilityLevel2(activator);
                        cooldownLevel2.put(activator.getUniqueId(), System.currentTimeMillis());
                    } else {
                        long remaining = COOLDOWN_LEVEL2 - (System.currentTimeMillis() - cooldownLevel2.get(activator.getUniqueId()));
                        activator.sendMessage("Ability +2 is on cooldown for " + (remaining / 1000) + " seconds.");
                    }
                    break;
                case 3:
                    if (canUseAbility(activator, 3)) {
                        abilityLevel3(activator);
                        cooldownLevel3.put(activator.getUniqueId(), System.currentTimeMillis());
                    } else {
                        long remaining = COOLDOWN_LEVEL3 - (System.currentTimeMillis() - cooldownLevel3.get(activator.getUniqueId()));
                        activator.sendMessage("Ability +3 is on cooldown for " + (remaining / 1000) + " seconds.");
                    }
                    break;
                default:
                    activator.sendMessage("You do not have a positive ability to use.");
                    break;
            }
        } else {
            activator.sendMessage("You do not have a positive ability to use.");
        }
    }

    private boolean canUseAbility(Player player, int abilityLevel) {
        UUID uuid = player.getUniqueId();
        long current = System.currentTimeMillis();
        return switch (abilityLevel) {
            case 1 -> {
                if (!cooldownLevel1.containsKey(uuid)) yield true;
                yield (current - cooldownLevel1.get(uuid)) >= COOLDOWN_LEVEL1;
            }
            case 2 -> {
                if (!cooldownLevel2.containsKey(uuid)) yield true;
                yield (current - cooldownLevel2.get(uuid)) >= COOLDOWN_LEVEL2;
            }
            case 3 -> {
                if (!cooldownLevel3.containsKey(uuid)) yield true;
                yield (current - cooldownLevel3.get(uuid)) >= COOLDOWN_LEVEL3;
            }
            default -> false;
        };
    }

    public String getCooldownStatus(Player player) {
        int level = levelManager.getLevel(player.getUniqueId());
        if (level <= 0) {
            return "N/A";
        }
        long current = System.currentTimeMillis();
        long totalCooldown = 0;
        long remaining = 0;
        switch(level) {
            case 1:
                totalCooldown = COOLDOWN_LEVEL1;
                remaining = cooldownLevel1.containsKey(player.getUniqueId())
                        ? COOLDOWN_LEVEL1 - (current - cooldownLevel1.get(player.getUniqueId())) : 0;
                break;
            case 2:
                totalCooldown = COOLDOWN_LEVEL2;
                remaining = cooldownLevel2.containsKey(player.getUniqueId())
                        ? COOLDOWN_LEVEL2 - (current - cooldownLevel2.get(player.getUniqueId())) : 0;
                break;
            case 3:
                totalCooldown = COOLDOWN_LEVEL3;
                remaining = cooldownLevel3.containsKey(player.getUniqueId())
                        ? COOLDOWN_LEVEL3 - (current - cooldownLevel3.get(player.getUniqueId())) : 0;
                break;
            default:
                break;
        }
        if (remaining <= 0) {
            return ChatColor.GREEN + "Ready" + ChatColor.RESET;
        } else {
            // Calcular la fracción restante
            double fraction = (double) remaining / totalCooldown;
            ChatColor cooldownColor;
            if (fraction > 0.5) {
                cooldownColor = ChatColor.RED; // Más del 50% restante
            } else if (fraction > 0.25) {
                cooldownColor = ChatColor.GOLD; // Entre el 25% y 50%
            } else {
                cooldownColor = ChatColor.YELLOW; // Menos o igual al 25%
            }
            long seconds = (remaining / 1000); // Redondear hacia arriba
            return "" + cooldownColor + seconds + "s" + ChatColor.RESET;
        }
    }

    // +1 Ability: Dispara 3 flechas con 4 puntos de daño cada una (2 corazones). Cooldown: 1 minuto. Grants Perm Hero of Village 3.
    private void abilityLevel1(Player activator) {
        activator.sendMessage("Activating ability +1: Shooting arrows and granting Hero of the Village 3.");
        World world = activator.getWorld();
        Location loc = activator.getEyeLocation();
        Vector baseDirection = loc.getDirection().normalize();
        for (int i = -1; i <= 1; i++) {
            Vector modifiedDirection = baseDirection.clone();
            modifiedDirection.rotateAroundY(Math.toRadians(10 * i));
            Arrow arrow = world.spawnArrow(loc, modifiedDirection, 1.5f, 12f);
            arrow.setDamage(3.0);
        }
    }

    // +2 Ability: Dash (lanza al jugador 15 bloques en la dirección que mira). Cooldown: 1 minuto. Grants Perm Hero of Village 6.
    private void abilityLevel2(Player activator) {
        activator.sendMessage("Activating ability +2: Dashing forward and granting Hero of the Village 6.");
        Vector dash = activator.getLocation().getDirection().normalize().multiply(15);
        activator.setVelocity(dash);
    }

    // +3 Ability: Invoca relámpagos sobre jugadores no confiables en un radio de 10 bloques,
    // aplica Haste 10 (amplifier 9) durante 15 segundos y causa 10 puntos de daño (5 corazones).
    // Cooldown: 3 minutos. Grants Perm Hero of Village 10.
    private void abilityLevel3(Player activator) {
        activator.sendMessage("Activating ability +3: Summoning lightning, dealing damage, and granting Hero of the Village 10.");
        Location loc = activator.getLocation();
        World world = activator.getWorld();
        Collection<Player> nearbyPlayers = world.getPlayers();
        for (Player target : nearbyPlayers) {
            if (target.equals(activator)) continue;
            if (target.getLocation().distance(loc) <= 10.0) {
                if (!TrustManager.isTrusted(activator.getUniqueId(), target.getUniqueId())) {
                    world.strikeLightningEffect(target.getLocation());
                    target.damage(10.0, activator);
                }
            }
        }
        activator.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 15 * 20, 9));
    }
}
