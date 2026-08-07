package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class ItemUtil extends Utility {

    public static PersistentDataContainer container(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer();
    }
}
