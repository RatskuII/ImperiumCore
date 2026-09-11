package dev.RatFjc.ImperiumCore.modules.stackresize;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public record Destination(ItemStack item, Inventory inventory, int destination) {
}
