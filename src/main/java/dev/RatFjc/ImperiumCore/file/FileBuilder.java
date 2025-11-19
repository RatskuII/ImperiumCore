package dev.RatFjc.ImperiumCore.file;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Global file manager for ImperiumCore. From now on, we should try to initialize all configs in this class
 * or another class in the file directory.
 */
public class FileBuilder {

    private final ImperiumCore plugin;
    private final File dataFile;
    private final YamlConfiguration dataConfig;

    public FileBuilder(ImperiumCore plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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