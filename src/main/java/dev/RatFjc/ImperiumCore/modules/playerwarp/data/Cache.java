package dev.RatFjc.ImperiumCore.modules.playerwarp.data;

import java.util.*;

/**
 * A cache allows the plugin to quickly query a frequently searched items without
 * constantly searching the database.
 */
public class Cache<F> {

    private final Map<UUID, F> cache;

    private Cache(WeakHashMap<UUID, F> cache) {
        this.cache = cache;
    }

    private Cache(Map<UUID, F> hashMap) {
        this.cache = hashMap;
    }

    /**
     * Adds a new value to the cache
     * @param id The uuid associated with this value
     * @param value The value to be cached
     * @return An updated {@link Cache}
     */
    public Cache<F> update(UUID id, F value) {
        cache.put(id, value);
        return this;
    }

    /**
     * Gets the value associated with this cache.
     * @param uuid A uuid key
     * @return The value, or null if none is present
     */
    public F get(UUID uuid) {
        return cache.get(uuid);
    }

    /**
     * Checks if the cache is available to be used. A cache is available for use when it's
     * not null and is updated to have at least one value.
     * @return Whether this cache is ready for use
     */
    public boolean available() {
        return cache != null && !cache.isEmpty();
    }

    public boolean contains(F element) {
        return cache.containsValue(element);
    }

    public boolean remove(UUID uuid) {
        cache.remove(uuid);
        return !cache.containsKey(uuid);
    }

    public static <F> Cache<F> create(WeakHashMap<UUID, F> cache) {
        return new Cache<>(cache);
    }

    public static <F> Cache<F> create() {
        Map<UUID, F> hashMap = new WeakHashMap<>();
        return new Cache<>(hashMap);
    }
 }
