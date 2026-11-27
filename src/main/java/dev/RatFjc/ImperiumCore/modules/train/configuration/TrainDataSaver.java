package dev.RatFjc.ImperiumCore.modules.train.configuration;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.extras.Reloadable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class TrainDataSaver extends ConfigurationSaver implements Reloadable {

    private static final File file = new File(plugin.getDataFolder(), "train.yml");
    private static FileConfiguration fileConfiguration = build(file);

    @Override
    public File file() {
        return file;
    }

    @Override
    public void set() {
        if (fileConfiguration == null) {
            nullFail("The configuration was not initialized.");
            return;
        }

        fileConfiguration.set("killskeleton", false);

        save(file, fileConfiguration, null);
    }

    @Override
    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static boolean skeletonKillingAllowed() {
        return fileConfiguration != null && fileConfiguration.getBoolean("killskeleton", false);
    }
}
