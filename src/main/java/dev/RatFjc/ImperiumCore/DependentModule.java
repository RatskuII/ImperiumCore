package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

/**
 * Represents a module that has dependencies.
 */
public interface DependentModule {

    List<@Nullable Plugin> dependencies();

    default void verify(Module module) {
        if (dependencies().isEmpty()) return;
        for (Plugin depend : dependencies()) {
            if (depend == null) {
                LogUtil.log("A dependency is missing from this module.", module, Level.WARNING, false);
                continue;
            }
            if (depend.isEnabled()) LogUtil.log("Dependency " + depend.getName() + " is available.", module, Level.INFO, false);
            else LogUtil.log("Could not find dependency: " + depend.getName() + ", is it enabled?", module, Level.WARNING, false);
        }
    }
}
