package dev.RatFjc.ImperiumCore.modules.afk.conf;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class TimerConfiguration extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "afk.yml");

    private static final FileConfiguration config = build(file);
    @Override
    protected void set() {
        if (config == null) {
            nullFail("Configuration not found.");
            return;
        }

        config.set("allowKick", true);
        config.set("kickTimer", 1800L);
    }

    public static long getKickTimer() {
        if (config == null) {
            nullFail("Configuration not found.");
            return 0;
        }
        return config.getLong("kickTimer", 1800);
    }

    public static boolean isKickAllowed() {
        if (config == null) {
            nullFail("Configuration not found.");
            return true;
        }
        return config.getBoolean("allowKick");
    }
}
