package dev.RatFjc.ImperiumCore.modules.playerwarp;

import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a location that is "owned" by a player. Players can teleport to these locations at will.
 */
public class Warp {

    private OfflinePlayer player;
    private Location location;
    private String name;
    private String description;
    private UUID uuid;
    private Rating rating = null;
    private WarpType warpType;

    private final Collection<String> blacklist = List.of(
            "create",
            "reset",
            "remove"
    );

    public Warp(OfflinePlayer player, Location location, String name, String description, WarpType warpType, UUID uuid) {
        this(player, location, name, description, warpType);
        this.uuid = uuid;
    }

    public Warp(OfflinePlayer player, Location location, String name, String description, WarpType warpType) {
        this.player = player;
        this.location = location;
        this.name = name;
        this.description = description;
        this.warpType = warpType;
        this.uuid = UUID.randomUUID();

        if (blacklist.contains(name)) throw new IllegalArgumentException("Illegal player warp name!");
    }

    /**
     * Gets the owner of this warp.
     * @return The warp owner
     */
    public OfflinePlayer owner() {
        return this.player;
    }

    /**
     * Gets the location of this warp.
     * @return A non-null location.
     */
    public Location location() {
        return this.location;
    }

    /**
     * Gets the name of this warp.
     * @return A name
     */
    public String name() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Gets a unique ID for this warp.
     * @return A UUID
     */
    public UUID uuid() {
        return this.uuid;
    }

    public WarpType getWarpType() {
        return this.warpType;
    }

    /**
     * Gets the rating assigned to this warp.
     * @return A rating, or null if none is assigned
     */
    public @Nullable Rating rating() {
        return this.rating;
    }

    public void setOwner(Player player) {
        this.player = player;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        if (blacklist.contains(name)) throw new IllegalArgumentException("Illegal player warp name!");
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setWarpType(WarpType warpType) {
        this.warpType = warpType;
    }
}
