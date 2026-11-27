package dev.RatFjc.ImperiumCore.init;

import dev.Fjc.localDiff.LocalDiff;
import dev.RatFjc.ImperiumCore.DependentModule;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.regions.cmd.CurrentRegionCmd;
import dev.RatFjc.ImperiumCore.modules.regions.config.RegionBuilder;
import dev.RatFjc.ImperiumCore.modules.regions.listener.RegionTicker;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Regions extends Module implements DependentModule, AsyncModule<ScheduledExecutorService> {

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

        BukkitUtil.registerCommand(new CurrentRegionCmd(), "region-info");
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

    @Override
    public List<@Nullable Plugin> dependencies() {
        return List.of(LocalDiff.getInstance());
    }
}
