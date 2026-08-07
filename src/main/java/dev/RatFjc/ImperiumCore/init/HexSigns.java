package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.hexsigns.SignAlter;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

public class HexSigns extends Module {
    @Override
    public String name() {
        return "HexSigns";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerEvent(new SignAlter());
    }
}
