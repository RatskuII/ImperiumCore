package dev.RatFjc.ImperiumCore.modules.petconverter;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvBuilder {

    private Component name;

    private final List<ItemStack> ui = new ArrayList<>();

    private static Inventory inventory;

    public InvBuilder create(Player player, int size, String title) {
        this.name = TextUtil.nbt(title);
        inventory = Bukkit.createInventory(player, size, name);
        return this;
    }

    public InvBuilder createUI() {
        ItemStack info = ItemStack.of(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(TextUtil.nbt("Dump old pets into this bin and receive compensation."));
        infoMeta.lore(List.of(
                TextUtil.color("Pets have been discontinued and serve no purpose other than historic means.", TextColor.color(100, 100, 100)),
                TextUtil.color("Putting pets into this exchange bin will provide you with silver currency.", TextColor.color(100, 100, 100)),
                TextUtil.color("The higher the pet level, the more silver you will get.", TextColor.color(100, 100, 100)),
                TextUtil.color("Be warned that once a pet is exchanged, it cannot be recovered!!!", TextColor.color(225, 40, 10))
        ));

        ItemStack close = ItemStack.of(Material.RED_WOOL);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(TextUtil.nbt("CLOSE"));
        closeMeta.lore(List.of(TextUtil.nbt("Click here to close this menu.")));

        info.setItemMeta(infoMeta);
        close.setItemMeta(closeMeta);

        Map<ItemStack, Integer> itemMap = new HashMap<>();
        itemMap.put(info, 0);
        itemMap.put(close, 1);
        return createUI(itemMap);
    }

    public InvBuilder createUI(Map<ItemStack, Integer> itemMap) {
        itemMap.forEach((item, integer) -> {
            inventory.setItem(integer, item);
            ui.add(item);

            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(Keys.PET_UI, PersistentDataType.BOOLEAN, true);

            item.setItemMeta(meta);
        });

        return this;
    }

    public final Inventory build() {
        return inventory;
    }

    public Component getName() {
        return this.name;
    }

    public List<ItemStack> getUi() {
        return this.ui;
    }
}
