package dev.RatFjc.ImperiumCore.file.configurations.trains;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.file.FileInterface;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * An implementation of {@link FileInterface} that handles all methods related to {@link dev.RatFjc.ImperiumCore.modules.train.AnnounceDelay}
 */
public class AnnounceDelayFiles extends FileBuilder implements FileInterface {

    public AnnounceDelayFiles(ImperiumCore plugin) {
        super(plugin);
    }

    @Override
    public YamlConfiguration getYaml() {
        return trainConfig;
    }

    @Override
    public File getFile() {
        return trainFile;
    }
}
