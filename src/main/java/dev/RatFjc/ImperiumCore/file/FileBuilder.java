package dev.RatFjc.ImperiumCore.file;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.file.configurations.quests.QuestCounterFiles;
import dev.RatFjc.ImperiumCore.file.configurations.trains.AnnounceDelayFiles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ServiceLoader;

/**
 * Global file manager for ImperiumCore. From now on, we should try to initialize all configs in this class
 * and utilize them in other classes.
 */
public class FileBuilder {

    protected final ImperiumCore plugin;

    // Quests
    protected final File questDataFile;
    protected final YamlConfiguration questDataConfig;

    protected final File questMainFile;
    protected final YamlConfiguration questMainConfig;

    // Trains
    protected final File trainFile;
    protected final YamlConfiguration trainConfig;

    public FileBuilder(ImperiumCore plugin) {
        this.plugin = plugin;

        this.questDataFile = new File(plugin.getDataFolder(), "data.yml");
        this.questDataConfig = YamlConfiguration.loadConfiguration(questDataFile);

        this.trainFile = new File(plugin.getDataFolder(), "trainaddons.yml");
        this.trainConfig = YamlConfiguration.loadConfiguration(trainFile);

        this.questMainFile = new File(plugin.getDataFolder(), "quests.yml");
        this.questMainConfig = YamlConfiguration.loadConfiguration(questMainFile);
    }

    /**
     * Builds files for every respective implementation of {@link FileInterface}.
     */
    public void build() {
        ImperiumCore.files.stream()
                .map(ServiceLoader.Provider::get)
                .forEach(action -> action.build(plugin, action.getYaml(), action.getFile(), null));
    }

    public static QuestCounterFiles getQuestCounterFiles(ImperiumCore plugin) {
        return new QuestCounterFiles(plugin);
    }

    public static AnnounceDelayFiles getAnnounceDelayFiles(ImperiumCore plugin) {
        return new AnnounceDelayFiles(plugin);
    }
}