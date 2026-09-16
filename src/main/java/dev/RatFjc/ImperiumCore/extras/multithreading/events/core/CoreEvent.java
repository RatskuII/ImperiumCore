package dev.RatFjc.ImperiumCore.extras.multithreading.events.core;

import dev.RatFjc.ImperiumCore.Module;
import org.bukkit.event.Event;

public abstract class CoreEvent extends Event {

    protected Module module;

    public CoreEvent(final boolean async, Module module) {
        super(async);
        this.module = module;
    }

    public final Module module() {
        return this.module;
    }
}
