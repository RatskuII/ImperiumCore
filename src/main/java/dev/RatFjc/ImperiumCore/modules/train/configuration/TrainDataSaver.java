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

        fileConfiguration.set("killskeleton", false);

        save(file, fileConfiguration, null);
    }

    public static boolean skeletonKillingAllowed() {
        return fileConfiguration != null && fileConfiguration.getBoolean("killskeleton", false);
    }
}
