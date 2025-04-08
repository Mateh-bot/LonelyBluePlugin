package org.example.lonelyBluePlugin.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class TrustManager {

    private static final HashMap<UUID, Set<UUID>> trustMap = new HashMap<>();
    private final DataManager dataManager;

    public TrustManager(DataManager dataManager) {
        this.dataManager = dataManager;
        loadTrust();
    }

    public static void addTrusted(UUID owner, UUID trusted) {
        trustMap.computeIfAbsent(owner, k -> new HashSet<>()).add(trusted);
    }

    public static boolean isTrusted(UUID owner, UUID other) {
        return trustMap.getOrDefault(owner, new HashSet<>()).contains(other);
    }

    public void loadTrust() {
        YamlConfiguration config = dataManager.getConfig();
        if (config.isConfigurationSection("trust")) {
            for (String key : config.getConfigurationSection("trust").getKeys(false)) {
                try {
                    UUID owner = UUID.fromString(key);
                    List<String> trustedList = config.getStringList("trust." + key);
                    Set<UUID> trustedSet = new HashSet<>();
                    for (String s : trustedList) {
                        try {
                            trustedSet.add(UUID.fromString(s));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    trustMap.put(owner, trustedSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveTrust() {
        YamlConfiguration config = dataManager.getConfig();
        for (HashMap.Entry<UUID, Set<UUID>> entry : trustMap.entrySet()) {
            List<String> list = new ArrayList<>();
            for (UUID uuid : entry.getValue()) {
                list.add(uuid.toString());
            }
            config.set("trust." + entry.getKey().toString(), list);
        }
        dataManager.saveData();
    }
}
