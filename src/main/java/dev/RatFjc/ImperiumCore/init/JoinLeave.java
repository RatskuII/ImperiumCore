package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.joinleave.JoinLeaveListener;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

public class JoinLeave extends Module {
    @Override
    public String name() {
        return "JoinLeave";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerEvent(new JoinLeaveListener());
    }
}
