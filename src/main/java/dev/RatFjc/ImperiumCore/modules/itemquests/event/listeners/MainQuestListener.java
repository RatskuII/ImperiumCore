package dev.RatFjc.ImperiumCore.modules.itemquests.event.listeners;

import dev.RatFjc.ImperiumCore.modules.itemquests.constuct.QuestItem;
import dev.RatFjc.ImperiumCore.modules.itemquests.progress.ProgressionSnapshot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MainQuestListener implements Listener {

    @EventHandler
    public void onQuestClick(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        Action action = event.getAction();

        if (itemStack == null) return;
        if (!QuestItem.isQuestItem(itemStack)) return;

        if (!action.isRightClick()) return;

        ProgressionSnapshot snapshot = QuestItem.getProgression(itemStack);
        if (snapshot == null) return;

        QuestItem questItem = new QuestItem<>(itemStack, null, snapshot);
    }
}
