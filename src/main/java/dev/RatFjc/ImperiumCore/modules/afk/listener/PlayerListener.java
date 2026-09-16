package dev.RatFjc.ImperiumCore.modules.afk.listener;

import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import dev.RatFjc.ImperiumCore.modules.afk.managers.TimerManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerListener extends AfkManager implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerMovementWatcher(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
        TimerManager.cancelKickTimer(player);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event) {
        // ** NEW **
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        // Check on the next tick
        // checks if the head orientation changed OR (if the health stayed the same and the player position changed)
        // if the health is different from the cache the player may have been externally attacked, return false
        Threader.execute(() -> {
            boolean condition0 = healthCache.get(player.getUniqueId()) != null;
            boolean condition1 = from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch(); // For now im only going to check this condition
            // boolean condition2 = player.getHealth() == healthCache.get(player.getUniqueId());
            boolean condition3 = from.distanceSquared(to) > 0;
            // boolean activate = condition0 && (condition1 || (condition2 && condition3));
            if (condition1) TimerManager.resetPreAfkTimer(player);
        });
        // ** NEW **
        // ** OLD **
        /*
        if (event.getFrom().distanceSquared(event.getTo()) > 0) TimerManager.resetPreAfkTimer(player);
        if (event.getFrom().getPitch() != event.getTo().getPitch()
                ||
                event.getFrom().getYaw() != event.getTo().getYaw()) TimerManager.resetPreAfkTimer(player);
         */
        // ** OLD **
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
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
        if (event.isAsynchronous()) {
            Threader.execute(() -> TimerManager.resetPreAfkTimer(player));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        TimerManager.resetPreAfkTimer(player);
    }

    // In theory this should not be needed anymore
    /*
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        List<Entity> passengers = event.getVehicle().getPassengers();
        passengers.forEach(entity -> {
            if (entity instanceof Player player) {
                TimerManager.resetPreAfkTimer(player);
            }
        });
    }

     */
}
