package dev.RatFjc.ImperiumCore.init.interfaces;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface BaseHook {

    String name();

    void load(ImperiumCore instance);

    @SuppressWarnings("UnstableApiUsage")
    default Logger log(String message, Level level) {
        Logger logger = Bukkit.getLogger();

        message = "[" + name() + "]" + " " + message;
        logger.log(level, message);

        return logger;
    }
}
