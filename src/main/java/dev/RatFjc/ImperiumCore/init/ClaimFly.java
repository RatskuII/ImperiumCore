package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.claimfly.FlyCommand;
import dev.RatFjc.ImperiumCore.modules.claimfly.FlyThread;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class ClaimFly extends Module {
    @Override
    public String name() {
        return "ClaimFly";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {
        FlyThread flyThread = new FlyThread();
        BukkitUtil.registerEvent(flyThread);
        flyThread.tick();

        BukkitUtil.registerCommand(new FlyCommand(), "fly");
    }
}
