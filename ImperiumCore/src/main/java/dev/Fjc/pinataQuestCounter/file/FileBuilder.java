package dev.Fjc.pinataQuestCounter.file;

import dev.Fjc.pinataQuestCounter.PinataQuestCounter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileBuilder {

    private final PinataQuestCounter plugin;
    private final File dataFile;
    private final YamlConfiguration dataConfig;

    public FileBuilder(PinataQuestCounter plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void build() {
        if (!dataFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                dataFile.createNewFile();
                dataConfig.set("counter", 0);
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int incrementCounter() {
        int current = dataConfig.getInt("counter", 0) + 1;
        dataConfig.set("counter", current);
        save();
        return current;
    }

    public void resetCounter() {
        dataConfig.set("counter", 0);
        save();
    }

    private void save() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml!");
            e.printStackTrace();
        }
    }
}