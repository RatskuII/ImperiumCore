package dev.RatFjc.ImperiumCore.init.interfaces;

import dev.RatFjc.ImperiumCore.ImperiumCore;

/**
 * Represents a module that will be loaded when ImperiumCore starts.
 */
public interface Module {

    String name();

    void load(ImperiumCore instance);
}
