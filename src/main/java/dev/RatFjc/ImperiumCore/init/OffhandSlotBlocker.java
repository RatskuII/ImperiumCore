package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.offhandslotblocker.OffhandListener;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class OffhandSlotBlocker extends Module {
    @Override
    public String name() {
        return "OffhandSlotBlocker";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new OffhandListener());
    }
}
