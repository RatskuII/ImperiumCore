package dev.RatFjc.ImperiumCore.modules.afk.listener;

import dev.RatFjc.ImperiumCore.modules.afk.managers.TimerManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerMovementWatcher(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().distanceSquared(event.getTo()) > 0) TimerManager.resetPreAfkTimer(player);
        if (event.getFrom().getPitch() != event.getTo().getPitch()
                ||
                event.getFrom().getYaw() != event.getTo().getYaw()) TimerManager.resetPreAfkTimer(player);
    }
    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TimerManager.startPreAfkTimer(player);
    }
    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimerManager.cancelPreAfkTimer(player);
    }
    @EventHandler()
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        TimerManager.resetPreAfkTimer(player);
    }
}
