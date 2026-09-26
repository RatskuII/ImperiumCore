package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import io.papermc.paper.persistence.PersistentDataContainerView;
import io.papermc.paper.persistence.PersistentDataViewHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.Nullable;

public class PDCUtil extends Utility {

    /**
     * Sets a value on the specified container.
     * @param container The container to set the value on
     * @param key The key to associate this value with
     * @param type A valid {@link PersistentDataType} that the output will conform to
     * @param output The value being set on the container
     * @param <R> Any {@link PersistentDataHolder}
     * @param <P> A primary (usually primitive) type
     * @param <C> The value/output's type
     * @return The specified container
     */
    public static <R extends PersistentDataHolder, P, C> R set(R container, NamespacedKey key, PersistentDataType<P, C> type, C output) {
        PersistentDataContainer result = container.getPersistentDataContainer();
        result.set(key, type, output);
        return container;
    }

    /**
     * Gets a value associated with the key in the specified container.
     * @param container The data holder
     * @param key The key associated with this value
     * @param type The {@link PersistentDataType} of this value
     * @return A value that conforms to the specified type
     * @param <R> An instance of the container
     * @param <P> The primary type
     * @param <C> The complex type
     */
    public static <R extends PersistentDataHolder, P, C> @Nullable C get(R container, NamespacedKey key, PersistentDataType<P, C> type) {
        PersistentDataContainer result = container.getPersistentDataContainer();
        return result.get(key, type);
    }

    public static <P, C> @Nullable C getView(PersistentDataViewHolder view, NamespacedKey key, PersistentDataType<P, C> type) {
        PersistentDataContainerView resultView = view.getPersistentDataContainer();
        return resultView.get(key, type);
    }

    public static <R extends PersistentDataHolder> void clear(R container, NamespacedKey key) {
        PersistentDataContainer result = container.getPersistentDataContainer();
        result.remove(key);
    }

    public static <P, C> boolean setOnItem(ItemStack item, NamespacedKey key, PersistentDataType<P, C> type, C output) {
        ItemMeta container = item.getItemMeta();
        set(container, key, type, output);
        return item.setItemMeta(container);
    }
}
