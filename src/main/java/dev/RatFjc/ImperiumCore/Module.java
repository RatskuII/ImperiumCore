package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.utility.LogUtil;

import java.util.concurrent.Executor;
import java.util.logging.Level;

/**
 * Represents a module that will be loaded when ImperiumCore starts.
 */
public abstract class Module {

    /**
     * The name of this plugin module.
     * @return The module name
     */
    public abstract String name();

    /**
     * Should this module be enabled?
     * @return Whether the module is enabled
     */
    public abstract boolean enabled();

    /**
     * What should this module do when the plugin enables? Leave empty for default.
     * @param instance The plugin being enabled
     */
    protected abstract void load(ImperiumCore instance);

    /**
     * What should this module do when the plugin is disabled? Leave empty for default.
     * @param instance The plugin being disabled
     * @apiNote Most modules won't need to implement this, as Bukkit already handles it. This is mostly useful for
     * external tasks or APIs that need to be shutdown or disabled explicitly.
     */
    protected void unload(ImperiumCore instance) {}

    public void postStartup() {
        LogUtil.log("Successfully loaded the module.", this, Level.INFO, false);
    }
}
