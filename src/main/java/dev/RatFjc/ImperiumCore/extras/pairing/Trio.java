package dev.RatFjc.ImperiumCore.extras.pairing;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Represents a three-way relationship of values. Unlike {@link Pair}, there isn't a key-value relationship enforced.
 * All three values are allowed to be null.
 * @param first The first value
 * @param second The second value
 * @param third The third value
 * @param <A> The first type
 * @param <B> The second type
 * @param <C> The third type
 */
public record Trio<A, B, C>(A first, B second, C third) {

    public static <A, B, C> @NotNull Trio<A, B, C> empty() {
        return new Trio<>(null, null, null);
    }

    public void apply(TriConsumer<A, B, C> consumer) {
        consumer.accept(first, second, third);
    }

    /**
     * An interface that represents possible operations on a {@link Trio} instance.
     * @param <A>
     * @param <B>
     * @param <C>
     */
    interface Transformer<A, B, C> {

        A apply(B second, C third);

        C transform(A first, B second);
        C transform(Trio<A, B, C> trio);

        Pair<A, B> reduce(BiConsumer<A, BiConsumer<B, C>> layeredConsumer);
    }
}
