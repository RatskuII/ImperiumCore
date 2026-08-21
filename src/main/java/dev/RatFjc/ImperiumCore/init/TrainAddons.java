package dev.RatFjc.ImperiumCore.init;

import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import dev.RatFjc.ImperiumCore.DependentModule;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.train.SkeletonRemover;
import dev.RatFjc.ImperiumCore.modules.train.SwitchSoundAdder;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.modules.train.AnnounceDelay;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;


public class TrainAddons extends Module implements DependentModule {

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
        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            try {
                Class.forName("com.bergerkiller.bukkit.tc.signactions.SignAction");
            } catch (ClassNotFoundException e) {
                LogUtil.log("Could not find the appropriate dependencies. That is a problem.", this, Level.SEVERE, false);
            }
        }, 1000L);
        register();
    }

    public static void register() {
        SignAction.register(new AnnounceDelay());

        BukkitUtil.registerEvent(new AnnounceDelay());
        BukkitUtil.registerEvent(new SwitchSoundAdder());
        BukkitUtil.registerEvent(new SkeletonRemover());
    }
    public static void unregister() {
        SignAction.unregister(new AnnounceDelay());
    }

    @Override
    public List<@Nullable Plugin> dependencies() {
        return List.of(
                TrainCarts.plugin
        );
    }
}
