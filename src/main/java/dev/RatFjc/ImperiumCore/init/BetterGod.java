package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.bettergod.command.GodCmd;
import dev.RatFjc.ImperiumCore.modules.bettergod.command.UnGodCmd;
import dev.RatFjc.ImperiumCore.modules.bettergod.handler.DamageIntercept;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

public class BetterGod extends Module {
    @Override
    public String name() {
        return "BetterGod";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerEvent(new DamageIntercept());

        EventUtil.registerCommand(new GodCmd(), "god");
        EventUtil.registerCommand(new UnGodCmd(), "ungod");
    }
}
