package dev.RatFjc.ImperiumCore.modules.itemquests.progress.handlers;

import org.bukkit.event.Event;

public interface ProgressionHandler<E extends Event> {

    boolean canProgress(E event);

    default void progress(E event) {

    }
}
