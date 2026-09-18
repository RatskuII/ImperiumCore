package dev.RatFjc.ImperiumCore.extras.listener;

import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChunkyTemp implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (PlayerUtil.getOnlinePlayerList().size() <= 1) BukkitUtil.dispatchCommand("chunky pause world");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (PlayerUtil.getOnlinePlayerList().isEmpty()) BukkitUtil.dispatchCommand("chunky continue world");
    }
}
