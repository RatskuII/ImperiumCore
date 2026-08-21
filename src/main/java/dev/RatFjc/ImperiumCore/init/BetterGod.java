package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.bettergod.command.GodCmd;
import dev.RatFjc.ImperiumCore.modules.bettergod.command.UnGodCmd;
import dev.RatFjc.ImperiumCore.modules.bettergod.handler.DamageIntercept;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

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
        BukkitUtil.registerEvent(new DamageIntercept());

        BukkitUtil.registerCommand(new GodCmd(), "god");
        BukkitUtil.registerCommand(new UnGodCmd(), "ungod");
    }
}
