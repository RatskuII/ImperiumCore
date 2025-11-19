package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.init.PetaPit;
import dev.RatFjc.ImperiumCore.init.PinataQuestCounter;
import dev.RatFjc.ImperiumCore.modules.pinataquesttracker.counter.CounterClass;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    private static ImperiumCore instance;

    private FileBuilder fileBuilder;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;
        this.fileBuilder = new FileBuilder(this);
        fileBuilder.build();
        PinataQuestCounter.load(this);
        PetaPit.load(this);

        if (getConfig().getBoolean("isEnabled", true)) {
            getServer().getPluginManager().registerEvents(new CounterClass(this), this);
            getLogger().info("PinataQuestCounter enabled and listening for quest completion events!");
        } else {
            getLogger().warning("Module disabled in config.yml (isEnabled: false)");
        }
    }

    @Override
    public void onDisable() {
        this.fileBuilder = null;
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }

    public static ImperiumCore getInstance() {
        return instance;
    }
}
