package dev.RatFjc.ImperiumCore.extras;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Represents a builder class that stores a mutable {@link Stream}. This is mainly just a convenience
 * class so that I don't have to keep reassigning a new stream (the builder will do it for me).
 * @param <T> The object type being streamed
 */
public final class Streamer<T> {

    private Stream<T> elements = Stream.empty();

    public Streamer() {}

    public Streamer(T... elements) {
        this.elements = Stream.of(elements);
    }

    public Streamer(Collection<T> elements) {
        this.elements = elements.stream();
    }

    public Streamer(Stream<T> elements) {
        this.elements = elements;
    }

    public Streamer<T> filter(Predicate<? super T> predicate) {
        elements = elements.filter(predicate);
        return this;
    }

    public <R> Streamer<R> map(Function<? super T, ? extends R> function) {
        Stream<R> result = elements.map(function);
        return new Streamer<>(result);
    }

    public <R> Streamer<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function) {
        Stream<R> result = elements.flatMap(function);
        return new Streamer<>(result);
    }

    public <R> Streamer<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> consumer) {
        Stream<R> result = elements.mapMulti(consumer);
        return new Streamer<>(result);
    }

    public Streamer<T> distinct() {
        elements = elements.distinct();
        return this;
    }

    public Streamer<T> sorted() {
        elements = elements.sorted();
        return this;
    }

    public Streamer<T> sorted(Comparator<? super T> comparator) {
        elements = elements.sorted(comparator);
        return this;
    }

    public Streamer<T> peek(Consumer<? super T> action) {
        elements = elements.peek(action);
        return this;
    }

    public Streamer<T> limit(long max) {
        elements = elements.limit(max);
        return this;
    }

    public Streamer<T> skip(long n) {
        elements = elements.skip(n);
        return this;
    }

    public Streamer<T> takeWhile(Predicate<? super T> predicate) {
        elements = elements.takeWhile(predicate);
        return this;
    }

    public Streamer<T> dropWhile(Predicate<? super T> predicate) {
        elements = elements.dropWhile(predicate);
        return this;
    }

    public Streamer<T> forEach(Consumer<? super T> action) {
        elements.forEach(action);
        return this;
    }

    public Streamer<T> forEachOrdered(Consumer<? super T> action) {
        elements.forEachOrdered(action);
        return this;
    }

    public Object[] array() {
        return elements.toArray();
    }

    public <A> A[] array(IntFunction<A[]> function) {
        return elements.toArray(function);
    }

    public T reduce(T identity, BinaryOperator<T> accumulate) {
        return elements.reduce(identity, accumulate);
    }

    public Optional<T> reduce(BinaryOperator<T> accumulate) {
        return elements.reduce(accumulate);
    }

    public <B> B reduce(B identity, BiFunction<B, ? super T, B> accumulate, BinaryOperator<B> combine) {
        return elements.reduce(identity, accumulate, combine);
    }

    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulate, BiConsumer<R, R> combine) {
        return elements.collect(supplier, accumulate, combine);
    }

    /**
     * Gets the resulting {@link Stream} constructed from prior operations and puts
     * them into a list. The order of said list depends on the order of the stream.
     * @return A non-null list representing the stream of objects
     */
    public @NotNull List<T> list() {
        return elements.toList();
    }

    /**
     * Gets the resulting {@link Stream} constructed from prior operations.
     * @return A non-null stream of objects
     */
    public @NotNull Stream<T> result() {
        return elements;
    }
}
