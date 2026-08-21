package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.ultrabans.command.BanCommand;
import dev.RatFjc.ImperiumCore.modules.ultrabans.event.listener.BanEnforcer;
import dev.RatFjc.ImperiumCore.modules.ultrabans.event.listener.MuteEnforcer;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class UltraBans extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String name() {
        return "UltraBans";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {

        BukkitUtil.registerEvent(new BanEnforcer());
        BukkitUtil.registerEvent(new MuteEnforcer());

        BukkitUtil.registerCommand(new BanCommand(), "ban");
    }

    @Override
    public Module module() {
        return this;
    }

    @Override
    public ScheduledExecutorService worker() {
        return EXECUTOR;
    }

    public static ScheduledExecutorService executor() {
        return EXECUTOR;
    }
}
