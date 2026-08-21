package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.nv.NightVisionCommand;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class NightVision extends Module {
    @Override
    public String name() {
        return "NV";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerCommand(new NightVisionCommand(), "nv");
    }
}
