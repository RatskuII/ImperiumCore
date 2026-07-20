package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.extras.cmds.ToggleExperimentals;
import dev.RatFjc.ImperiumCore.extras.listener.OnByDefault;
import dev.RatFjc.ImperiumCore.init.*;
import dev.RatFjc.ImperiumCore.utility.EventUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    private static ImperiumCore instance;

    // Modules
    private PetaPit petaPit;
    private TrainAddons trainAddons;
    private JoinLeave joinLeave;
    private NightVision nightVision;
    private Afk afk;

    @Override
    public void onEnable() {
        instance = this;
        init();

        load(petaPit);
        load(trainAddons);
        load(joinLeave);
        load(nightVision);
        load(afk);

        EventUtil.registerCommand(new ToggleExperimentals(), "allow-experiments");
        EventUtil.registerEvent(new OnByDefault());
    }

    @Override
    public void onDisable() {
        shutdown();
        instance = null;
    }

    public static ImperiumCore getInstance() {
        if (instance == null) {
            instance = new ImperiumCore();
            return instance;
        }

        return instance;
    }

    public void init() {
        petaPit = new PetaPit();
        trainAddons = new TrainAddons();
        joinLeave = new JoinLeave();
        nightVision = new NightVision();
        afk = new Afk();
    }

    public void load(Module module) {
        if (!module.enabled()) return;
        module.load(instance);
        module.postStartup();
    }

    private void shutdown() {
        TrainAddons.unregister();

        petaPit = null;
        trainAddons = null;
        joinLeave = null;
        nightVision = null;
        afk = null;

    }
}
