package dev.RatFjc.ImperiumCore.extras;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a list of pairs. This is just a map with extra steps.
 * @param <K> The key's type
 * @param <V> The value's type
 */
public final class PairList<K, V> {

    private final List<Pair<K, V>> pairList;

    public PairList() {
        this.pairList = new ArrayList<>();
    }

    public PairList(List<Pair<K, V>> pairList) {
        this.pairList = pairList;
    }

    public void add(Pair<K, V> pair) {
        this.pairList.add(pair);
    }

    public void add(K key, V value) {
        Pair<K, V> pair = new Pair<>(key, value);
        this.pairList.add(pair);
    }

    public @Nullable V get(K key) {
        for (Pair<K, V> pair : pairList) {
            if (pair.key() == key) return pair.value();
        }
        return null;
    }

    public void addAll(Collection<? extends Pair<K, V>> pairs) {
        pairList.addAll(pairs);
    }

    public boolean remove(Pair<K, V> pair) {
        return pair != null && pairList.remove(pair);
    }

    public boolean modifyPair(K key, V value, boolean selector) {
        for (Pair<K, V> pair : pairList) {
            int index = pairList.indexOf(pair);
            if (selector) {
                if (pair.key() == key) {
                    Pair<K, V> result = new Pair<>(key, value);
                    pairList.set(index, result);
                    return pairList.get(index) == result;
                }
            } else {
                if (pair.value() == value) {
                    Pair<K, V> result = new Pair<>(key, value);
                    pairList.set(index, result);
                    return pairList.get(index) == result;
                }
            }

        }
        return false;
    }
}
