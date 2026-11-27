package dev.RatFjc.ImperiumCore.modules.regions;

import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.init.Regions;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

    public <E extends Entity> E spawn(Location location, Class<E> mob, Consumer<E> before) {
        return world().spawn(location, mob, before);
    }

    /**
     * Spawns a mob next to another mob within this region.
     * @param entity The entity to spawn next to
     * @param mob The class of the mob that should be spawned
     * @param before A function that runs before the mob is spawned
     * @return The spawned mob, or null if the operation failed
     * @param <E> The entity type
     */
    public <E extends Entity> E spawn(Entity entity, Class<E> mob, Consumer<E> before) {
        if (entity == null) return null;
        return spawn(entity.getLocation(), mob, before);
    }

    /**
     * Spawns a mob in a pseudorandom location within the region boundary.
     * @param mob The class of the mob that should be spawned
     * @return The spawned mob, or null if the operation failed
     * @param <E> The entity type
     */
    public <E extends Entity> CompletableFuture<E> spawn(Class<E> mob) {
        Random random = new Random();
        double pos0 = random.nextDouble(bounds().key(), bounds().value());
        double pos1 = random.nextDouble(bounds().key(), bounds().value());
        int y = world().getHighestBlockYAt((int) pos0, (int) pos1);
        Location location = new Location(world(), pos0, y, pos1);
        CompletableFuture<E> futureResult = world().getChunkAtAsync(location).thenApply(fn -> spawn(location, mob, null));
        return DataUtil.getOrNull(Threader.supplyFuture(() -> futureResult), null, 3);
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
