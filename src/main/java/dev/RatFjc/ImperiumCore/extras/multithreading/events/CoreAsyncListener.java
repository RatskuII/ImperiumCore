package dev.RatFjc.ImperiumCore.extras.multithreading.events;

import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.concurrent.Executor;

public interface CoreAsyncListener<E extends Event, M extends AsyncModule<?>> extends Listener {

    M module();

    void onEvent(E event, Executor worker);

    default void handler(E event) {
        onEvent(event, module().worker());
    }
}
