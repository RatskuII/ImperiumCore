package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;

public class ItemQuests extends Module {
    @Override
    public String name() {
        return "ItemQuests";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {
    }
}
