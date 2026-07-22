package dev.RatFjc.ImperiumCore.modules.offhandslotblocker;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class OffhandListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack target = event.getCursor();
        InventoryType inventoryType = event.getInventory().getType();

        if (target.getType() != Material.PIG_SPAWN_EGG) return;
        if (inventoryType != InventoryType.CRAFTING && inventoryType != InventoryType.PLAYER) return;

        if (event.getSlot() == 40) {
            event.setCancelled(true);
            TextUtil.sendMessage(player, "You are not allowed to put this item into your offhand.");
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getOffHandItem();

        if (item.getType() == Material.PIG_SPAWN_EGG) {
            event.setCancelled(true);
            TextUtil.sendMessage(player, "You are not allowed to put this item into your offhand.");
        }
    }
}
