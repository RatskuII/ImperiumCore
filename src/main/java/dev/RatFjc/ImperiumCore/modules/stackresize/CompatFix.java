package dev.RatFjc.ImperiumCore.modules.stackresize;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.utility.PDCUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public final class CompatFix {

    private static final Random random = new Random();

    public static void tick(ItemStack itemStack) {
        if (itemStack.getAmount() == 0) return;

        PDCUtil.setOnItem(itemStack, Keys.DUMMY, PersistentDataType.LONG, random.nextLong());
        Threader.execute(() -> {
           if (!itemStack.hasItemMeta()) return;
           ItemMeta secondary = itemStack.getItemMeta();
           PDCUtil.clear(secondary, Keys.DUMMY);
           itemStack.setItemMeta(secondary);
        });
    }

    public static ItemStack reduce(ItemStack item, int amount) {
        item.setAmount(item.getAmount() - amount);
        return item;
    }

    public static void reduce(ItemStack item) {
        reduce(item, 1);
    }
}
