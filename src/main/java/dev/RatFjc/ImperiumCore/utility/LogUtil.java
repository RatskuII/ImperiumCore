package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Utility;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class LogUtil extends Utility {

    /**
     * A logger function, represented by the specified {@link Module} module plugin
     * @param message The message to send to the logger
     * @param module The module represented by this log, or null to represent the main class.
     * @param level The severity of the log
     * @param debug Whether this message should be treated as debug
     */
    @SuppressWarnings("UnstableApiUsage")
    public static void log(String message, @Nullable Module module, Level level, boolean debug) {
        String prefix = "[" + ((module != null) ? module.name() : ImperiumCore.class.getSimpleName()) + "] ";
        if (debug) prefix += "[DEBUG] ";
        Bukkit.getLogger().log(level, prefix + message);
    }

    /**
     * Log via the main plugin instance (NOT a module).
     * @param message The message to log
     */
    public static void log(String message) {
        log(message, null, Level.INFO, false);
    }
}
