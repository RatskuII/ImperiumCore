package dev.RatFjc.ImperiumCore.modules.itemquests.progress;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.QuestState;
import dev.RatFjc.ImperiumCore.modules.itemquests.constuct.QuestItem;
import dev.RatFjc.ImperiumCore.modules.itemquests.nbt.ProgressionSaver;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class Progression {

    private float progress;
    private final float total;

    private QuestState questState;

    protected Progression(float total) {
        this.progress = 0;
        this.total = total;
        this.questState = QuestState.SEALED;
    }

    protected Progression(float progress, float total) {
        this.progress = progress;
        this.total = total;
        this.questState = QuestState.ACTIVE;
    }

    protected Progression(float progress, float total, QuestState state) {
        this.progress = progress;
        this.total = total;
        this.questState = state;
    }

    public void progress() {
        this.progress++;
    }

    public void progress(float prog) {
        this.progress += prog;
    }

    public float getProgress() {
        return this.progress;
    }

    public float getTotal() {
        return this.total;
    }

    public QuestState getQuestState() {
        return this.questState;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setQuestState(QuestState state) {
        this.questState = state;
    }

    public void setComplete() {
        this.questState = QuestState.COMPLETE;
    }

    public void save(QuestItem<? extends AbstractQuest> questItem) {
        ItemStack itemStack = questItem.itemStack();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        ProgressionSnapshot snapshot = ProgressionSnapshot.snapshot(this, questItem);
        container.set(Keys.PROGRESSION, new ProgressionSaver(), snapshot);
        questItem.saveToItem(meta);
    }
}
