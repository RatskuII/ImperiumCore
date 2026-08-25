package dev.RatFjc.ImperiumCore.modules.itemquests.progress.handlers;

import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.constuct.QuestItem;
import org.bukkit.event.Event;

public interface ProgressionHandler<E extends Event> {

    boolean canProgress(E event);

    void hook(E event);

    default void progress(E event, QuestItem<? extends AbstractQuest> questItem) {
        if (canProgress(event)) questItem.advance();
    }
}
