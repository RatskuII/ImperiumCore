package dev.RatFjc.ImperiumCore.modules.regions.events;

import dev.RatFjc.ImperiumCore.modules.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class RegionEvent extends Event {

    protected final Player player;

    protected final Region region;

    protected RegionEvent(final Player player, final Region region) {
        this.player = player;
        this.region = region;
    }

    /**
     * Represents the player involved in this region event.
     * @return The player
     */
    public final @NotNull Player player() {
        return this.player;
    }

    /**
     * Represents the region involved in this region event.
     * @return The region
     */
    public final Region region() {
        return this.region;
    }
}
