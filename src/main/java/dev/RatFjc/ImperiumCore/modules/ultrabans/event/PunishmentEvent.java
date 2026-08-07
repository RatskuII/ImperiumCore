package dev.RatFjc.ImperiumCore.modules.ultrabans.event;

import dev.RatFjc.ImperiumCore.modules.ultrabans.Punishment;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called right after a new punishment is built, but also right before any actual
 * operation is performed. If canceled, the entry will be temporarily saved but no action will be
 * taken against the target.
 * @param <T> The target type
 */
public class PunishmentEvent<T> extends Event implements Cancellable {

    private final Punishment<T> punishment;

    private boolean canceled = false;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PunishmentEvent(Punishment<T> punishment) {
        this.punishment = punishment;
    }

    public Punishment<T> getPunishment() {
        return this.punishment;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}
