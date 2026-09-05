package dev.RatFjc.ImperiumCore.init;

import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import dev.RatFjc.ImperiumCore.DependentModule;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.train.*;
import dev.RatFjc.ImperiumCore.modules.train.configuration.TrainDataSaver;
import dev.RatFjc.ImperiumCore.modules.train.signadder.AnnounceDelay;
import dev.RatFjc.ImperiumCore.modules.train.signadder.TrainPopulator;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;


public class TrainAddons extends Module implements DependentModule {

    private final AnnounceDelay announceDelay = new AnnounceDelay();
    private final TrainPopulator trainPopulator = new TrainPopulator();

    @Override
    public String name() {
        return "TrainAddons";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void load(ImperiumCore instance) {
        fileSetup(new TrainDataSaver());

        SignAction.register(announceDelay);
        SignAction.register(trainPopulator);

        BukkitUtil.registerEvent(announceDelay);
        BukkitUtil.registerEvent(new SwitchSoundAdder());
        BukkitUtil.registerEvent(new SkeletonRemover());
        BukkitUtil.registerEvent(trainPopulator);

        BukkitUtil.registerCommand(new RouteInfo(), "route-lookup");

        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            try {
                Class.forName("com.bergerkiller.bukkit.tc.signactions.SignAction");
            } catch (ClassNotFoundException e) {
                LogUtil.log("Could not find the appropriate dependencies. That is a problem.", this, Level.SEVERE, false);
            }
        }, 1000L);

    }

    @Override
    public void unload(ImperiumCore instance) {
        SignAction.unregister(announceDelay);
        SignAction.unregister(trainPopulator);
    }

    @Override
    public List<@Nullable Plugin> dependencies() {
        return List.of(
                TrainCarts.plugin
        );
    }
}
