package dev.RatFjc.ImperiumCore.modules.itemquests.constuct;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.nbt.ProgressionSaver;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.Progression;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.ProgressionSnapshot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class QuestItem<Q extends AbstractQuest> {

    private final ItemStack itemStack;

    private final Q quest;

    private UUID uuid;

    public QuestItem(ItemStack itemStack, Q quest, Progression progression) {
        this.itemStack = itemStack;
        this.quest = quest;
        this.uuid = UUID.randomUUID();

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        ProgressionSnapshot snapshot = ProgressionSnapshot.snapshot(progression, this);
        container.set(Keys.PROGRESSION, new ProgressionSaver(), snapshot);
        itemStack.setItemMeta(itemMeta);
    }

    public QuestItem(ItemStack itemStack, Q quest, Progression progression, UUID uuid) {
        this(itemStack, quest, progression);
        this.uuid = uuid;
    }

    public ItemStack itemStack() {
        return this.itemStack;
    }

    public void saveToItem(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
    }

    public Q quest() {
        return this.quest;
    }

    public final UUID uuid() {
        return this.uuid;
    }
}
