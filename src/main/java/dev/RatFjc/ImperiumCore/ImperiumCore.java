package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.init.*;
import dev.RatFjc.ImperiumCore.modules.pinataquesttracker.counter.CounterClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    private static ImperiumCore instance;

    // File
    private FileBuilder fileBuilder;

    // Modules
    private PetaPit petaPit;
    private PinataQuestCounter pinataQuestCounter;
    private TrainAddons trainAddons;
    private JoinLeave joinLeave;
    private NightVision nightVision;

    @Override
    public void onEnable() {
        instance = this;
        init();

        fileBuilder.build();

        load(petaPit);
        load(pinataQuestCounter);
        load(trainAddons);
        load(joinLeave);
        load(nightVision);
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

    public void init() {
        fileBuilder = new FileBuilder(instance);

        petaPit = new PetaPit();
        pinataQuestCounter = new PinataQuestCounter();
        trainAddons = new TrainAddons();
        joinLeave = new JoinLeave();
        nightVision = new NightVision();
    }

    public void load(Module module) {
        if (!module.enabled()) return;
        module.load(instance);
    }

    private void shutdown() {
        TrainAddons.unregister();

        petaPit = null;
        pinataQuestCounter = null;
        trainAddons = null;
        joinLeave = null;
        nightVision = null;

    }
}
