package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.pinataquesttracker.counter.CounterClass;

public class PinataQuestCounter extends Module {

    @Override
    public String name() {
        return "PinataQuestCounter";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public void load(ImperiumCore instance) {
        if (instance.getConfig().getBoolean("isEnabled", true)) {
            instance.getServer().getPluginManager().registerEvents(new CounterClass(instance), instance);
            instance.getLogger().info("PinataQuestCounter enabled and listening for quest completion events!");
        } else {
            instance.getLogger().warning("Module disabled in config.yml (isEnabled: false)");
        }
    }
}