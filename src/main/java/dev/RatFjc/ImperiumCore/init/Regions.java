package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.regions.config.RegionBuilder;
import dev.RatFjc.ImperiumCore.modules.regions.listener.RegionTicker;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Regions extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String name() {
        return "Regions";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        RegionBuilder.loadAll().thenRun(() -> fileSetup(new RegionBuilder()));

        RegionTicker regionTicker = new RegionTicker();
        BukkitUtil.registerEvent(regionTicker);
    }

    @Override
    public Module module() {
        return this;
    }

    @Override
    public ScheduledExecutorService worker() {
        return executor;
    }

    public static Executor executor() {
        return executor;
    }
}
