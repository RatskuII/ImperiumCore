package dev.RatFjc.ImperiumCore.modules.afk.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.hooks.PacketHook;
import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import dev.RatFjc.ImperiumCore.modules.afk.managers.TimerManager;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerListener extends AfkManager implements Listener {

    private static Tracker tracker;

    public PlayerListener() {
        tracker = new Tracker(plugin, ListenerPriority.HIGH,
                PacketType.Play.Client.LOOK,
                PacketType.Play.Client.POSITION_LOOK);

        PacketHook.manager.addPacketListener(tracker);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerMovementWatcher(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        TimerManager.resetPreAfkTimer(player);
        TimerManager.cancelKickTimer(player);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        Threader.execute(() -> {
            boolean condition1 = from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch();
            if (condition1) TimerManager.resetPreAfkTimer(player);
        });
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

    public static class Tracker extends PacketAdapter {

        public Tracker(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
            super(plugin, listenerPriority, types);
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            Player player = event.getPlayer();
            Threader.execute(() -> TimerManager.resetPreAfkTimer(player));
        }
    }
}
