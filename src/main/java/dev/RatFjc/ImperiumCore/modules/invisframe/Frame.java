package dev.RatFjc.ImperiumCore.modules.invisframe;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Frame {

    private ItemStack itemStack = ItemStack.of(Material.ITEM_FRAME);

    private final ItemMeta meta;

    public Frame() {
        this.meta = itemStack.getItemMeta();
    }

    public Frame amount(int amount) {
        itemStack = itemStack.asQuantity(amount);
        return this;
    }

    public Frame glow() {
        this.itemStack = ItemStack.of(Material.GLOW_ITEM_FRAME);
        return this;
    }

    public Frame name(String name) {
        meta.displayName(TextUtil.nbt(name));
        return this;
    }

    public Frame enchant() {
        return enchant(Enchantment.UNBREAKING, 1);
    }

    public Frame enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        return this;
    }

    public Frame flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public Frame addLore(String loreLine) {
        var lore = meta.lore();
        if (lore == null) return this;

        lore.add(TextUtil.nbt(loreLine));
        meta.lore(lore);

        return this;
    }

    public final ItemStack build() {
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Keys.INVIS_FRAME, PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    // Static utility methods

    public static boolean isFrame(Hanging hanging) {
        PersistentDataContainer container = hanging.getPersistentDataContainer();
        return match(hanging) && container.has(Keys.INVIS_FRAME);
    }

    public static boolean match(Hanging hanging) {
        return hanging.getType() == EntityType.ITEM_FRAME
                || hanging.getType() == EntityType.GLOW_ITEM_FRAME;
    }

    public static boolean isGlowing(Hanging hanging) {
        PersistentDataContainer container = hanging.getPersistentDataContainer();
        var value = container.get(Keys.GLOW_FRAME, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

}
