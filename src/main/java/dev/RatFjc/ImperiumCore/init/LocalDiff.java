package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LocalDiff extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String name() {
        return "LocalDiff";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {

    }

    @Override
    public Module module() {
        return this;
    }

    @Override
    public ScheduledExecutorService worker() {
        return EXECUTOR;
    }

    public static Executor executor() {
        return EXECUTOR;
    }
}
