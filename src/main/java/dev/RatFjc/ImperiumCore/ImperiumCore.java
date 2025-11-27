package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.file.FileInterface;
import dev.RatFjc.ImperiumCore.init.TrainAddons;
import dev.RatFjc.ImperiumCore.init.interfaces.Module;
import dev.RatFjc.ImperiumCore.modules.pinataquesttracker.counter.CounterClass;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    private static ImperiumCore instance;

    // Loaders
    public static @ApiStatus.Experimental ServiceLoader<FileInterface> files;
    public static @ApiStatus.Experimental ServiceLoader<Module> main;

    // File
    private FileBuilder fileBuilder;

    @Override
    public void onEnable() {
        instance = this;
        main = ServiceLoader.load(Module.class);
        files = ServiceLoader.load(FileInterface.class);
        this.fileBuilder = new FileBuilder(this);
        fileBuilder.build();

        initialize();

        if (getConfig().getBoolean("isEnabled", true)) {
            getServer().getPluginManager().registerEvents(new CounterClass(this), this);
            getLogger().info("PinataQuestCounter enabled and listening for quest completion events!");
        } else {
            getLogger().warning("Module disabled in config.yml (isEnabled: false)");
        }
    }

    @Override
    public void onDisable() {
        shutdown();
        this.fileBuilder = null;
        instance = null;
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }

    public static ImperiumCore getInstance() {
        if (instance == null) {
            instance = new ImperiumCore();
            return instance;
        }

        return instance;
    }

    private void initialize() {
        main.stream()
                .map(ServiceLoader.Provider::get)
                .forEach(obj -> obj.load(this));
    }

    private void shutdown() {
        files = null;
        main = null;

        TrainAddons.unregister();

    }
}
