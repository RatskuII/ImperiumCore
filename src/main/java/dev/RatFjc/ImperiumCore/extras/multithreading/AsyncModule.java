package dev.RatFjc.ImperiumCore.extras.multithreading;

import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.utility.LogUtil;

import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * Represents a module that can run async operations. Implementations should have
 * a static Executor method.
 */
public interface AsyncModule<P extends Executor> {

    Module module();

    /**
     * Represents the worker thread (or threads) that this module can use. If not implemented,
     * this will default to the main server thread.
     * @return An executor handling these threads
     */
    P worker();

    default CompletableFuture<Void> operation(Runnable action) {
        return CompletableFuture.runAsync(action, worker());
    }

    default void displayWarning() {
        LogUtil.log("This module may run async operations.", module(), Level.WARNING, true);
    }

}
