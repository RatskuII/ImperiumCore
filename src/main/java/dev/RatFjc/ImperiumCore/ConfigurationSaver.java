package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.init.PlayerWarps;
import dev.RatFjc.ImperiumCore.init.UltraBans;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;

/**
 * Represents an abstract class that can be inherited as a configuration helper class. The class includes
 * static utility methods like {@link #build(File)} and {@link #save(File, FileConfiguration, Executor)}.
 */
public abstract class ConfigurationSaver {

    protected static final ImperiumCore plugin = ImperiumCore.getInstance();

    /**
     * Sets up the file to allow read/writes.
     * @param file The file to target. If the file does not exist, it will be created.
     * @return A new {@link FileConfiguration} that can be accessed for this file, or null if something went wrong.
     */
    protected static @Nullable FileConfiguration build(File file) {
        if (!file.exists()) plugin.getDataFolder().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            LogUtil.log("An error occurred while trying to load a file: " + e.getMessage(), null, Level.WARNING, false);
            return null;
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    protected static <U> CompletableFuture<U> nullFail(String thrown) {
        return CompletableFuture.failedFuture(new NullPointerException(thrown));
    }

    /**
     * Saves the provided configuration to the specified file.
     * @param file The file to save to
     * @param fileConfiguration The configuration to save
     * @return A successful future where the config is saved, or a failed future if something went wrong.
     */
    protected static CompletableFuture<Void> save(File file, FileConfiguration fileConfiguration, Executor executor) {
        if (file == null || fileConfiguration == null) return nullFail("File or configuration is missing or corrupt.");
        if (executor == null) return CompletableFuture.runAsync(() -> {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                LogUtil.log("Something went wrong while trying to save a file.", new UltraBans(), Level.SEVERE, false);
                LogUtil.log(e.getMessage());
            }
        });
        return CompletableFuture.runAsync(() -> {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                LogUtil.log("Something went wrong while trying to save a file.", new UltraBans(), Level.SEVERE, false);
                LogUtil.log(e.getMessage());
            }
        }, executor);
    }
}
