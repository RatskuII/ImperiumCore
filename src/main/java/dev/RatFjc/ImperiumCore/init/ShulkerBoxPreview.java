package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.shulkerboxpreview.ShulkerBoxListener;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class ShulkerBoxPreview extends Module {
    @Override
    public String name() {
        return "ShulkerBoxPreview";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new ShulkerBoxListener());
    }
}
