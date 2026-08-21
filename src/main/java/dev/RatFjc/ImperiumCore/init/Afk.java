package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.afk.listener.PlayerListener;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class Afk extends Module {
    @Override
    public String name() {
        return "AFK";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new PlayerListener());
    }
}
