package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.invisframe.command.GetFrameCommand;
import dev.RatFjc.ImperiumCore.modules.invisframe.listener.FrameEventHandler;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class InvisFrame extends Module {
    @Override
    public String name() {
        return "InvisFrame";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new FrameEventHandler());
        BukkitUtil.registerCommand(new GetFrameCommand(), "frame");
    }
}
