package dev.RatFjc.ImperiumCore.file.configurations.quests;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.file.FileInterface;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * A standalone class that handles all methods related to PinataQuestCounter files.
 */
public class QuestCounterFiles extends FileBuilder implements FileInterface {

    public QuestCounterFiles(ImperiumCore plugin) {
        super(plugin);
        build(plugin, questDataConfig, questDataFile, action -> {
            action.set("counter", 0);
        });
    }

    public int incrementCounter() {
        int current = questDataConfig.getInt("counter", 0) + 1;
        questDataConfig.set("counter", current);
        save(plugin, questDataConfig, questDataFile);
        return current;
    }

    public void resetCounter() {
        questDataConfig.set("counter", 0);
        save(plugin, questDataConfig, questDataFile);
    }

    @Override
    public YamlConfiguration getYaml() {
        return questDataConfig;
    }

    @Override
    public File getFile() {
        return questDataFile;
    }
}
