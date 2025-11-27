package dev.RatFjc.ImperiumCore.init.interfaces;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public abstract class PluginLogger {

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
}
