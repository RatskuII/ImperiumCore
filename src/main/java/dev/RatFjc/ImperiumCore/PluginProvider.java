package dev.RatFjc.ImperiumCore;

/**
 * A simple interface that provides access to the main plugin instance.
 */
public interface PluginProvider {

    ImperiumCore plugin = ImperiumCore.getInstance();
}
