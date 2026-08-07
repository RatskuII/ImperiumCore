package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.playerwarp.command.PWCommand;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerWarps extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String name() {
        return "PlayerWarps";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerCommand(new PWCommand(), "pw");
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

    @Override
    public CompletableFuture<Void> operation(Runnable action) {
        return AsyncModule.super.operation(action);
    }

    @Override
    public void displayWarning() {
        AsyncModule.super.displayWarning();
    }
}
