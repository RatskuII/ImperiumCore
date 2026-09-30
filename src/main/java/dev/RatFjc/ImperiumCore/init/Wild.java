package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.wild.Rtp;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Wild extends Module implements AsyncModule<ExecutorService> {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    public String name() {
        return "WildTP";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerCommand(new Rtp(), "rtp");
        fileSetup(new Rtp());
    }

    @Override
    public Module module() {
        return this;
    }

    @Override
    public ExecutorService worker() {
        return EXECUTOR;
    }

    public static Executor executor() {
        return EXECUTOR;
    }
}
