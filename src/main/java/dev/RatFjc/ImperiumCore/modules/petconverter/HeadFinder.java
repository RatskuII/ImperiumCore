package dev.RatFjc.ImperiumCore.modules.petconverter;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.utility.ItemUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HeadFinder {

    private static final List<ItemStack> remainders = new ArrayList<>();

    public static List<ItemStack> getValidHeads(Inventory inventory) {
        List<ItemStack> filter = new ArrayList<>();

        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            if (item.getType() != Material.PLAYER_HEAD) {
                remainders.add(item);
                continue;
            }

            var meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                remainders.add(item);
                continue;
            }

            Component customName = meta.customName();
            if (customName == null) customName = meta.itemName();
            String name = TextUtil.data(customName)
                    .toLowerCase();
            if (!name.contains("pet")) {
                remainders.add(item);
                continue;
            }
            filter.add(item);
        }
        return filter;
    }

    public static boolean isUI(ItemStack item) {
        return ItemUtil.container(item).has(Keys.PET_UI);
    }

    // Make sure to filter out UI elements
    public static List<ItemStack> getRemainders() {
        return remainders.stream()
                .filter(Objects::nonNull)
                .filter(obj -> !isUI(obj))
                .toList();
    }

    public static void clear() {
        remainders.clear();
    }
}
