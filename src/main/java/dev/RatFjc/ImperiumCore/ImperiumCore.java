package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.file.FileInterface;
import dev.RatFjc.ImperiumCore.init.PetaPit;
import dev.RatFjc.ImperiumCore.init.PinataQuestCounter;
import dev.RatFjc.ImperiumCore.init.TrainAddons;
import dev.RatFjc.ImperiumCore.modules.pinataquesttracker.counter.CounterClass;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    // Loaders
    public static @ApiStatus.Experimental ServiceLoader<FileInterface> loader;
    private static ImperiumCore instance;

    // File
    private FileBuilder fileBuilder;

    // Bases
    private PinataQuestCounter pinataQuestCounter;
    private TrainAddons trainAddons;
    private PetaPit petaPit;



    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;
        loader = ServiceLoader.load(FileInterface.class);
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

    public void initialize() {
        this.pinataQuestCounter = new PinataQuestCounter();
        this.trainAddons = new TrainAddons();
        this.petaPit = new PetaPit();

        pinataQuestCounter.load(this);
        trainAddons.load(this);
        petaPit.load(this);
    }

    public void shutdown() {
        loader = null;

        TrainAddons.unregister();
        this.pinataQuestCounter = null;
        this.trainAddons = null;
        this.petaPit = null;
    }
}
