package dev.RatFjc.ImperiumCore.file;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Represents an abstract interface that builds and saves files to the disk.
 */
public interface FileInterface {

    /**
     * A default implementation to construct new files when the plugin loads.
     * @param plugin The plugin instance
     * @param file The file you want to build
     * @param action A stateless function to execute after completion, can be null
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void build(ImperiumCore plugin, YamlConfiguration yaml, File file, @Nullable Consumer<YamlConfiguration> action) {
        if (!file.exists()) try {
            plugin.getDataFolder().mkdirs();
            file.createNewFile();
            save(plugin, yaml, file);

            if (action != null) action.accept(yaml);
        } catch (IOException exception) {
            plugin.getLogger().warning("Something went wrong while attempting to construct: " + file.getName());
            plugin.getLogger().warning(exception.getLocalizedMessage());
        }
    }

    /**
     * A default implementation to save files.
     * @param plugin The plugin this is associated with
     * @param yaml The YamlConfiguration to save
     * @param file The file being saved to
     */
    default void save(ImperiumCore plugin, YamlConfiguration yaml, File file) {
        try {
            yaml.save(file);
        } catch (IOException exception) {
            plugin.getLogger().warning("Something went wrong while attempting to save: " + file.getName());
            plugin.getLogger().warning(exception.getLocalizedMessage());
        }
    }

    YamlConfiguration getYaml();

    File getFile();
}
