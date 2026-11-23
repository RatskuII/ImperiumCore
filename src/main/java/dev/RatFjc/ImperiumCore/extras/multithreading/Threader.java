package dev.RatFjc.ImperiumCore.extras.multithreading;

import dev.RatFjc.ImperiumCore.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Threader implements PluginProvider {

    private static final Executor corePool = Executors.newCachedThreadPool();

    private static final Executor main = Bukkit.getScheduler().getMainThreadExecutor(plugin);

    /**
     * Guarantees that the provided action will run on the main thread.
     *
     * @param task The runnable task
     */
    public static void execute(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    public static BukkitTask tick(@Nullable Runnable task, long interval) {
        return Bukkit.getScheduler().runTaskTimer(plugin, Objects.requireNonNullElseGet(task, () -> () -> {
        }), 0, interval);
    }

    /**
     * Supplies a future that is guaranteed to run on the main thread.
     * @param input A stateless supplier function
     * @return A future that will be resolved on the main thread
     * @param <F> The type being supplied
     */
    public static <F> CompletableFuture<F> supplyFuture(Supplier<F> input) {
        return CompletableFuture.supplyAsync(input, main);
    }

    public static Executor corePool() {
        return corePool;
    }


}
