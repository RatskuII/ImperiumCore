package dev.Fjc.pinataQuestCounter;

import dev.Fjc.pinataQuestCounter.counter.CounterClass;
import dev.Fjc.pinataQuestCounter.file.FileBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class PinataQuestCounter extends JavaPlugin {

    private FileBuilder fileBuilder;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        fileBuilder = new FileBuilder(this);
        fileBuilder.build();

        if (getConfig().getBoolean("isEnabled", true)) {
            getServer().getPluginManager().registerEvents(new CounterClass(this), this);
            getLogger().info("PinataQuestCounter enabled and listening for quest completion events!");
        } else {
            getLogger().warning("Plugin disabled in config.yml (isEnabled: false)");
        }
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }
}