package dev.RatFjc.ImperiumCore.extras.multithreading.events;

import org.bukkit.event.Event;

public abstract class CoreEvent<M extends Module> extends Event {
    private M module;

    private boolean fake;
    private boolean async;

    protected CoreEvent(M module) {
        this.module = module;
        this.async = false;
        this.fake = false;
    }

    protected CoreEvent(M module, boolean fake) {
        this.module = module;
        this.fake = fake;
        this.async = false;
    }

    protected CoreEvent(M module, boolean fake, boolean async) {
        this.module = module;
        this.fake = fake;
        this.async = async;
    }


}
