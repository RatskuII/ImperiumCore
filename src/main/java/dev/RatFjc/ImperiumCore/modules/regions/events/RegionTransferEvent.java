package dev.RatFjc.ImperiumCore.modules.regions.events;

import dev.RatFjc.ImperiumCore.modules.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player moves into a new region.
 */
public class RegionTransferEvent extends RegionEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Region oldRegion;

    public RegionTransferEvent(Player player, Region region, @Nullable Region oldRegion) {
        super(player, region);
        this.oldRegion = oldRegion;
    }

    /**
     * Represents the region that the player was previously in.
     * @return The specified region, or null if not applicable
     */
    public @Nullable Region oldRegion() {
        return this.oldRegion;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
