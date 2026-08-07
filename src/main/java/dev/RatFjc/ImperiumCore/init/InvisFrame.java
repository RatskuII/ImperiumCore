package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.invisframe.command.GetFrameCommand;
import dev.RatFjc.ImperiumCore.modules.invisframe.listener.FrameEventHandler;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

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
        EventUtil.registerEvent(new FrameEventHandler());
        EventUtil.registerCommand(new GetFrameCommand(), "frame");
    }
}
