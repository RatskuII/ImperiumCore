package dev.RatFjc.ImperiumCore.modules.playerwarp.event;

import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a warp is newly created. If canceled, the creation will not occur.
 */
public class WarpCreateEvent extends Event implements Cancellable {

    private final Warp warp;

    private boolean cancel;

    public WarpCreateEvent(Warp warp) {
        this.warp = warp;
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Warp getWarp() {
        return this.warp;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
