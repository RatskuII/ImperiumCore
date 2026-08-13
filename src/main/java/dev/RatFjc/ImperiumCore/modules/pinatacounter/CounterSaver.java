package dev.RatFjc.ImperiumCore.modules.pinatacounter;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.init.PinataQuestCounter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class CounterSaver extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "pinata.yml");

    private static final FileConfiguration fileConfiguration = build(file);

    public static void set() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return;
        }
        // Counter
        fileConfiguration.set("counter", 0);

        // Settings
        fileConfiguration.set("settings.quests_cap", 30);
        fileConfiguration.set("settings.progress_complete_command", "pinata spawn");

        // Messages
        fileConfiguration.set("messages.spawn", "The pinata was spawned as a result of quests being completed.");
        fileConfiguration.set("messages.progress", "Progress has progressed by 1.");
        save(file, fileConfiguration, PinataQuestCounter.executor());
    }

    public static int countIncrement() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return -1;
        }
        int current = getCounter();
        fileConfiguration.set("counter", current++);
        save(file, fileConfiguration, PinataQuestCounter.executor());
        return current;
    }

    public static int getCounter() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return -1;
        }
        return fileConfiguration.getInt("counter", 0);
    }

    public static int getCap() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return -1;
        }
        return fileConfiguration.getInt("settings.quests_cap", 2);
    }

    public static String getSpawnMsg() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return "";
        }
        return fileConfiguration.getString("messages.spawn", "");
    }

    public static String getCompletionCmd() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return "";
        }
        return fileConfiguration.getString("settings.progress_complete_command", "");
    }

    public static String getProgressMsg() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return "";
        }
        return fileConfiguration.getString("messages.progress", "");
    }

    public static void resetCounter() {
        if (fileConfiguration == null) {
            nullFail("Configuration is unavailable.");
            return;
        }
        fileConfiguration.set("counter", 0);
        save(file, fileConfiguration, PinataQuestCounter.executor());
    }
}
