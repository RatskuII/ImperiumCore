package dev.RatFjc.ImperiumCore.init;

import com.bergerkiller.bukkit.tc.signactions.SignAction;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.init.interfaces.Module;
import dev.RatFjc.ImperiumCore.init.interfaces.PluginLogger;
import dev.RatFjc.ImperiumCore.modules.train.AnnounceDelay;

import java.util.logging.Level;

public class TrainAddons implements Module {

    @Override
    public String name() {
        return "TrainAddons";
    }

    @Override
    public void load(ImperiumCore instance) {
        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            try {
                Class.forName("com.bergerkiller.bukkit.tc.signactions.SignAction");
            } catch (ClassNotFoundException e) {
                PluginLogger.log("Could not find the appropriate dependencies. That is a problem.", this, Level.SEVERE, false);
            }
        }, 1000L);
        register();
    }

    public static void register() {
        SignAction.register(new AnnounceDelay());
    }
    public static void unregister() {
        SignAction.unregister(new AnnounceDelay());
    }
}
