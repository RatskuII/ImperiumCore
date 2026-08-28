package dev.RatFjc.ImperiumCore.modules.train.configuration;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class TrainDataSaver extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "train.yml");
    private static final FileConfiguration fileConfiguration = build(file);

    @Override
    public void set() {
        if (fileConfiguration == null) {
            nullFail("The configuration was not initialized.");
            return;
        }
        plugin.saveResource("train.yml", false);

        fileConfiguration.set("enabled", true);
        fileConfiguration.set("chance", 0.1f);
        fileConfiguration.set("killskeleton", false);
    }

    public static float getCurrentChance() {
        if (fileConfiguration == null) return 0f;
        return (float) fileConfiguration.getDouble("chance", 0.1f);
    }

    public static boolean isEnabled() {
        return fileConfiguration != null && fileConfiguration.getBoolean("enabled", true);
    }

    public static boolean skeletonKillingAllowed() {
        return fileConfiguration != null && fileConfiguration.getBoolean("killskeleton", false);
    }
}
