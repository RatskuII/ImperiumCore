package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.lastdeath.DeathStore;
import dev.RatFjc.ImperiumCore.modules.lastdeath.command.LastDeathCmd;
import dev.RatFjc.ImperiumCore.modules.lastdeath.event.DeathLocationUpdater;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class LastDeath extends Module {
    @Override
    public String name() {
        return "LastDeath";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new DeathLocationUpdater());
        BukkitUtil.registerCommand(new LastDeathCmd(), "lastdeath");
    }

    @Override
    protected void unload(ImperiumCore instance) {
        DeathStore.clear();
    }
}
