package dev.RatFjc.ImperiumCore.modules.itemquests.constuct;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.nbt.ProgressionSaver;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.Progression;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.ProgressionSnapshot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class QuestItem<Q extends AbstractQuest> {

    private final ItemStack itemStack;

    private final Progression progression;

    private final Q quest;

    private UUID uuid;

    public QuestItem(ItemStack itemStack, Q quest, Progression progression) {
        this.itemStack = itemStack;
        this.quest = quest;
        this.progression = progression;
        this.uuid = UUID.randomUUID();

        saveProgress();
    }

    public QuestItem(ItemStack itemStack, Q quest, Progression progression, UUID uuid) {
        this(itemStack, quest, progression);
        this.uuid = uuid;
    }

    public static boolean isQuestItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        var value = container.get(Keys.QUEST, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

    public static @Nullable ProgressionSnapshot getProgression(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(Keys.PROGRESSION, new ProgressionSaver());
    }

    public ItemStack itemStack() {
        return this.itemStack;
    }

    public void saveToItem(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
    }

    public void advance() {
        advance(1);
    }

    public void advance(float progress) {
        this.progression.progress(progress);
        saveProgress();
    }

    public Q quest() {
        return this.quest;
    }

    public final UUID uuid() {
        return this.uuid;
    }

    private void saveProgress() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        ProgressionSnapshot snapshot = ProgressionSnapshot.snapshot(this.progression, this);
        if (!isQuestItem(itemStack)) container.set(Keys.QUEST, PersistentDataType.BOOLEAN, true);
        container.set(Keys.PROGRESSION, new ProgressionSaver(), snapshot);
        this.saveToItem(itemMeta);
    }
}
