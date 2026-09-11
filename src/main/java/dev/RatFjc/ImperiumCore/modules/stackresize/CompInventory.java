package dev.RatFjc.ImperiumCore.modules.stackresize;

import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;

public abstract class CompInventory implements Inventory {

    private InventoryHolder inventoryHolder;

    private int maxStackSize;

    public CompInventory(int maxStackSize) {
        this.inventoryHolder = null;
        this.maxStackSize = maxStackSize;
    }

    public CompInventory(InventoryHolder holder, int maxStackSize) {
        this(maxStackSize);
        this.inventoryHolder = holder;
    }

    public static List<Integer> asList(Integer... slots) {
        return DataUtil.arrayToList(slots);
    }

    public static boolean firstSlotMatch(Inventory inventory, ItemStack item) {
        ItemStack check = Arrays.stream(inventory.getContents()).findFirst().orElse(null);
        if (check == null || check.getAmount() == 0) return false;
        return check.isSimilar(item);
    }

    @Override
    public void setMaxStackSize(int size) {
        this.maxStackSize = size;
    }
}
