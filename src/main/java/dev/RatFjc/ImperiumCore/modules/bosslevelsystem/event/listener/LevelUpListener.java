package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.event.listener;

import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.event.LevelUpEvent;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUpListener implements Listener {

    @EventHandler
    public void onLevelUp(LevelUpEvent event) {
        String output = event.message();
        if (output != null) BukkitUtil.broadcast(output);
    }
}
