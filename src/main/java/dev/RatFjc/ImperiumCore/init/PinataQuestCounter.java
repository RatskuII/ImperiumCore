package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.init.interfaces.BaseHook;

public class PinataQuestCounter implements BaseHook {

    @Override
    public String name() {
        return "PinataQuestCounter";
    }

    @Override
    public void load(ImperiumCore instance) {
        instance.saveDefaultConfig();
    }
}