package dev.RatFjc.ImperiumCore.extras.multithreading;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Threader {

    private static final ImperiumCore plugin = ImperiumCore.getInstance();

    public static BukkitTask execute(Runnable task) {
        return Bukkit.getScheduler().runTask(plugin, task);
    }
}
