package dev.RatFjc.ImperiumCore.modules.petconverter.listener;

import dev.RatFjc.ImperiumCore.modules.petconverter.HeadData;
import dev.RatFjc.ImperiumCore.modules.petconverter.HeadFinder;
import dev.RatFjc.ImperiumCore.modules.petconverter.InvBuilder;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class PetExchangerInventoryListener implements Listener {

    // Make sure to exclude UI elements from interactions
    @EventHandler
    public void keepUI(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InvBuilder builder = new InvBuilder();
        builder
                .create(player, 18, "Pet Exchanger")
                .createUI();

        Component comparable = event.getView().title();

        if (!comparable.equals(builder.getName())) return;

        ItemStack target = event.getCurrentItem();
        if (target == null) return;
        for (ItemStack item : builder.getUi()) {
            if (item.isSimilar(target)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        InvBuilder builder = new InvBuilder();
        builder
                .create(player, 18, "Pet Exchanger")
                .createUI();

        Component title = event.getView().title();
        if (!title.equals(builder.getName())) return;

        Inventory inventory = event.getInventory();
        String bad = "One or more items provided are not a valid pet.";
        HeadFinder.clear();
        List<ItemStack> validItems = HeadFinder.getValidHeads(inventory);
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            if (!validItems.contains(item)) {
                TextUtil.sendMessage(player, bad);
                continue;
            }

            String itemName = TextUtil.data(item.displayName());
            HeadData.getLevel(itemName).ifPresentOrElse(present -> {
                if (present >= 1) execute(player, present);
                if (present == 0) TextUtil.sendMessage(player, bad);
            },
                    () -> HeadData.getLevelFromLore(item.lore()).ifPresentOrElse(present -> {
                        if (present >= 1) execute(player, present);
                        else TextUtil.sendMessage(player, bad);
                    }, () -> TextUtil.sendMessage(player, bad)));
        }
        returnItems(player);
    }

    private void execute(Player player, int petLevel) {
        int total = Math.max((int) (petLevel * 0.15), 1);
        BukkitUtil.dispatchCommand("silver give " + player.getName() + " " + total);
        TextUtil.sendMessage(player, "You received " + total + " silver for trading in a level " + petLevel + " pet.");
    }

    private void returnItems(Player player) {
        for (ItemStack item : HeadFinder.getRemainders()) {
            HashMap<Integer, ItemStack> extras = player.getInventory().addItem(item);
            if (!extras.isEmpty()) for (ItemStack dropped : extras.values()) {
                World world = player.getWorld();
                world.dropItemNaturally(player.getLocation(), dropped,
                        onDrop -> TextUtil.sendMessage(player, "An item did not fit in your inventory and was dropped on the ground."));
            }
        }
        HeadFinder.clear();
    }
}
