package dev.RatFjc.ImperiumCore.extras;

/**
 * Intended for {@link dev.RatFjc.ImperiumCore.ConfigurationSaver} implementations. Represents a configuration
 * that can be safely reloaded.
 */
public interface Reloadable {

    void reload();
}
