package dev.RatFjc.ImperiumCore.extras.pairing;

import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

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

    /**
     * Converts a {@link java.util.Map.Entry} into a {@link Pair} instance.
     * @param entry The map entry to be converted
     * @return The resulting {@link Pair}, not null. Will return an empty pair if the entry is null
     * @param <K> The key's type
     * @param <V> The value's type
     */
    public static <K, V> @NotNull Pair<K, V> fromEntry(Map.Entry<K, V> entry) {
        if (entry == null) return empty();
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    /**
     * Applies an operation on both the key and the value.
     * @param consumer A consumer that represents the operation
     */
    public void apply(BiConsumer<K, V> consumer) {
        consumer.accept(key, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object pair) {
        if (this == pair) return true;
        if (pair == null) return false;
        if (getClass() != pair.getClass()) return false;

        Pair<?, ?> compare = (Pair<?, ?>) pair;
        return DataUtil.equalsAll(key, compare.key) && DataUtil.equalsAll(value, compare.value);
    }
}
