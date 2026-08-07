package dev.RatFjc.ImperiumCore.modules.shulkerboxpreview;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ShulkerBoxListener implements Listener {

    @EventHandler
    public void onShulkerClick(InventoryClickEvent event) {
        final String display = "Shulker Box Preview";
        Component title = event.getView().title();

        if (title.contains(TextUtil.nbt(display))) event.setCancelled(true);
        else if (event.getClick() == ClickType.RIGHT) {
            ItemStack current = event.getCurrentItem();
            if (current == null) return;

            ItemMeta meta = current.getItemMeta();
            if (meta == null) return;

            if (meta instanceof BlockStateMeta blockStateMeta) {
                if (!(blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox)) return;

                Inventory dummy = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, TextUtil.nbt(display));
                dummy.setContents(shulkerBox.getInventory().getContents());

                if (!(event.getWhoClicked() instanceof Player player)) return;
                player.setItemOnCursor(null);
                player.closeInventory();
                player.openInventory(dummy);
            }
        }
    }
}
