package dev.RatFjc.ImperiumCore.modules.itemquests.event.listeners;

import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.constuct.QuestItem;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.handlers.ProgressionHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener implements Listener, ProgressionHandler<EntityDeathEvent> {
    @Override
    public boolean canProgress(EntityDeathEvent event) {
        return false;
    }

    @Override
    public void hook(EntityDeathEvent event) {

    }
}
