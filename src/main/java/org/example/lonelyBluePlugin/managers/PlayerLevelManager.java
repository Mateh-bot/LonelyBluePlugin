package org.example.lonelyBluePlugin.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLevelManager {

    private final HashMap<UUID, Integer> playerLevels = new HashMap<>();
    private final DataManager dataManager;

    public PlayerLevelManager(DataManager dataManager) {
        this.dataManager = dataManager;
        loadLevels();
    }

    public int getLevel(UUID uuid) {
        return playerLevels.getOrDefault(uuid, 0);
    }

    public void setLevel(UUID uuid, int level) {
        playerLevels.put(uuid, level);
    }

    public void increaseLevel(UUID uuid) {
        int current = getLevel(uuid);
        if (current < 3) {
            playerLevels.put(uuid, current + 1);
        }
    }

    public void decreaseLevel(UUID uuid) {
        int current = getLevel(uuid);
        if (current > -3) {
            playerLevels.put(uuid, current - 1);
        }
    }

    public boolean upgradeNegativeLevel(UUID uuid) {
        int current = getLevel(uuid);
        if (current < 0) {
            playerLevels.put(uuid, current + 1);
            return true;
        }
        return false;
    }

    public void loadLevels() {
        YamlConfiguration config = dataManager.getConfig();
        if (config.isConfigurationSection("levels")) {
            for (String key : config.getConfigurationSection("levels").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    int level = config.getInt("levels." + key);
                    playerLevels.put(uuid, level);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveLevels() {
        YamlConfiguration config = dataManager.getConfig();
        for (Map.Entry<UUID, Integer> entry : playerLevels.entrySet()) {
            config.set("levels." + entry.getKey().toString(), entry.getValue());
        }
        dataManager.saveData();
    }
}
