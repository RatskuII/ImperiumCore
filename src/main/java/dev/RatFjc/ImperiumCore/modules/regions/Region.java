package dev.RatFjc.ImperiumCore.modules.regions;

import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.init.Regions;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a ring-shaped boundary zone within a world. A region also has its own set of properties defined
 * within {@link RegionProperty}, which determine the mechanics of the region.
 */
public abstract class Region {

    /**
     * The world that this region is set in.
     * @return A valid World
     */
    public abstract World world();

    /**
     * Defined properties that affect the behavior and mechanics of the region.
     * @return A {@link RegionProperty} instance that holds properties
     */
    public abstract RegionProperty properties();

    /**
     * The boundaries that establish this region.
     * @return A {@link Pair} representing the boundary of the region
     */
    public abstract Pair<Integer, Integer> bounds();

    public UUID uuid() {
        return UUID.randomUUID();
    }

    public static Region of(World world, RegionProperty property, Pair<Integer, Integer> bounds) {
        return new Region() {
            @Override
            public World world() {
                if (world == null) return Bukkit.getWorld("world");
                return world;
            }

            @Override
            public RegionProperty properties() {
                return property;
            }

            @Override
            public Pair<Integer, Integer> bounds() {
                return bounds;
            }
        };
    }

    public static Region of(World world, RegionProperty property, Pair<Integer, Integer> bounds, UUID uuid) {
        if (uuid == null) throw new NullPointerException("Null UUID is not allowed here.");
        return new Region() {
            @Override
            public World world() {
                if (world == null) return Bukkit.getWorld("world");
                return world;
            }

            @Override
            public RegionProperty properties() {
                return property;
            }

            @Override
            public Pair<Integer, Integer> bounds() {
                return bounds;
            }

            @Override
            public UUID uuid() {
                return uuid;
            }
        };
    }

    public static class Cache {
        private static final Vector<Region> cache = new Vector<>();

        public static synchronized CompletableFuture<Boolean> add(Region region) {
            return CompletableFuture.supplyAsync(() -> cache.add(region), Regions.executor());
        }

        public static synchronized CompletableFuture<Boolean> remove(Region region) {
            return CompletableFuture.supplyAsync(() -> cache.remove(region), Regions.executor());
        }
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof Region compared)) return false;

        RegionProperty otherProperty = compared.properties();
        Pair<Integer, Integer> otherBounds = compared.bounds();
        UUID otherId = compared.uuid();
        return properties().equals(otherProperty) && bounds().equals(otherBounds) && uuid().equals(otherId);
    }
}
