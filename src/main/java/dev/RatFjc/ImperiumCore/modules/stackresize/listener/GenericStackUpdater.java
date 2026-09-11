package dev.RatFjc.ImperiumCore.modules.stackresize.listener;

import dev.RatFjc.ImperiumCore.modules.stackresize.CompInventory;
import dev.RatFjc.ImperiumCore.modules.stackresize.data.StackSize;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GenericStackUpdater implements Listener {

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        player.updateInventory();
    }

    @EventHandler
    public void onShift(InventoryClickEvent event) {
        if (event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) return;

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        Inventory destination = event.getView().getTopInventory();
        if (inventory.equals(destination)) destination = event.getView().getBottomInventory();

        ItemStack current = event.getCurrentItem();
        if (current == null || current.getAmount() == 0) return;
        if (!StackSize.changed(current.getType())) return;
        if (!CompInventory.firstSlotMatch(destination, current)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onStack(InventoryClickEvent event) {

    }
}
