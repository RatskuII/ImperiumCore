package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class ItemUtil extends Utility {

    public static PersistentDataContainer container(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer();
    }

    public static ItemStack itemFromString(String value, int amount) {
        if (value == null) return ItemStack.empty();
        Material material = Material.getMaterial(value);
        if (material == null) return ItemStack.empty();
        return ItemStack.of(material, amount);
    }
}
