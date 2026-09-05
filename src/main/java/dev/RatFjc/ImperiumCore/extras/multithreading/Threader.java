package dev.RatFjc.ImperiumCore.extras.multithreading;

import dev.RatFjc.ImperiumCore.PluginProvider;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Threader implements PluginProvider {

    private static Executor corePool = Executors.newCachedThreadPool();

    /**
     * Guarantees that the provided action will run on the main thread.
     * @param task The runnable task
     * @return The resulting {@link BukkitTask}
     */
    public static BukkitTask execute(Runnable task) {
        return Bukkit.getScheduler().runTask(plugin, task);
    }

    public static void execute(Runnable task, Executor executor) {
        executor.execute(task);
    }

    public static Executor corePool() {
        return corePool;
    }


}
