package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.playerwarp.command.PWCommand;
import dev.RatFjc.ImperiumCore.modules.playerwarp.database.types.SQLite;
import dev.RatFjc.ImperiumCore.modules.playerwarp.event.listener.WarpCreationListener;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// This module will remain disabled until I'm ready to use it again. In the meantime I'll find a free plugin.
public class PlayerWarps extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    public static SQLite sqLite;

    @Override
    public String name() {
        return "PlayerWarps";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {
        sqLite = new SQLite();
        sqLite.connection();
        sqLite.initialize();
        EventUtil.registerCommand(new PWCommand(), "pw");

        EventUtil.registerEvent(new WarpCreationListener());
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
