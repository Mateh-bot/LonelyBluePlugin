package org.example.lonelyBluePlugin.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.example.lonelyBluePlugin.Main;

import java.io.File;
import java.io.IOException;

public class DataManager {

    private final Main main;
    private File dataFile;
    private YamlConfiguration dataConfig;

    public DataManager(Main main) {
        this.main = main;
        createDataFile();
    }

    private void createDataFile() {
        // Se crea la carpeta del plugin (si no existe) y el archivo data.yml
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdirs();
        }
        dataFile = new File(main.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public YamlConfiguration getConfig() {
        return dataConfig;
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
}
