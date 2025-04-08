package org.example.lonelyBluePlugin.updaters;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.lonelyBluePlugin.managers.PlayerLevelManager;

public class PlayerEffectUpdater implements Runnable {

    private final PlayerLevelManager levelManager;

    public PlayerEffectUpdater(PlayerLevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int level = levelManager.getLevel(player.getUniqueId());
            // Primero se remueven algunos efectos que pueden re-aplicarse.
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SLOWNESS);
            // También se remueve el efecto "Hero of the Village" para re-aplicarlo.
            player.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);

            if (level <= 0) {
                if (level == 0) {
                    // Nivel 0: Fire Resistance permanente (se re-aplica cada 60 segundos).
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0, true, false));
                }
                if (level == -3) {
                    // Nivel -3: Slowness permanente.
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30 * 20, 0, true, false));
                }
            } else {
                // Para niveles positivos se aplica el efecto Hero of the Village de forma permanente.
                // Suponemos que se usa PotionEffectType.HERO_OF_THE_VILLAGE; el amplifier se calcula como (valor - 1).
                switch(level) {
                    case 1:
                        // "Hero of the Village 3" se implementa con amplifier 2.
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 30 * 20, 2, true, false));
                        break;
                    case 2:
                        // "Hero of the Village 6" se implementa con amplifier 5.
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 30 * 20, 5, true, false));
                        break;
                    case 3:
                        // "Hero of the Village 10" se implementa con amplifier 9.
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 30 * 20, 9, true, false));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
