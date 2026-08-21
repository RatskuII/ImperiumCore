package dev.RatFjc.ImperiumCore.extras;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a simple key-value pair, which holds only one key and only one value. Both the key and value
 * are allowed to be null.
 * @param key A key
 * @param value A value
 * @param <K> The key's type
 * @param <V> The value's type
 */
public record Pair<K, V>(K key, V value) {

    /**
     * Represents an empty {@link Pair}, which holds one null key and one null value. This can be useful to represent
     * an empty result/no result, instead of returning null explicitly.
     * @return An empty {@link Pair}, never null
     */
    public static <K, V> @NotNull Pair<K, V> empty() {
        return new Pair<>(null, null);
    }
}
