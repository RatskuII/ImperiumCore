package dev.RatFjc.ImperiumCore.modules.mailbox;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MailReminder implements Listener, PluginProvider {

    private final MailDatabase db;
    private final FileConfiguration cfg;
    private final FileConfiguration messages;

    // stores either the pending delayed task id (initial delay) OR the repeating task id after swap
    private final Map<UUID, Integer> reminderTasks = new HashMap<>();

    public MailReminder(MailDatabase db, FileConfiguration cfg, FileConfiguration messages) {
        this.db = db;
        this.cfg = cfg;
        this.messages = messages;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPermission("imperium.mail.reminder"))
            return;

        startReminder(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        stopReminder(e.getPlayer());
    }

    public void startReminder(Player p) {

        stopReminder(p);

        long loginDelay = cfg.getLong("Mailbox.reminder.login_delay_ticks", 100L);
        long interval = cfg.getLong("Mailbox.reminder.periodic_ticks", 1000L);

        // Schedule initial delayed task that will send the first reminder after loginDelay ticks,
        // then deposit a repeating task id into the map for periodic reminders.
        int initialTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            if (!p.isOnline()) return;
            if (!p.hasPermission("imperium.mail.reminder")) return;

            sendReminder(p);

            // schedule repeating task
            int repeatId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

                if (!p.isOnline()) {
                    stopReminder(p);
                    return;
                }

                if (!p.hasPermission("imperium.mail.reminder")) {
                    stopReminder(p);
                    return;
                }

                sendReminder(p);

            }, interval, interval);

            // replace stored id with repeating id
            reminderTasks.put(p.getUniqueId(), repeatId);

        }, loginDelay);

        reminderTasks.put(p.getUniqueId(), initialTaskId);
    }

    public void stopReminder(Player p) {
        Integer id = reminderTasks.remove(p.getUniqueId());
        if (id != null)
            Bukkit.getScheduler().cancelTask(id);
    }

    private void sendReminder(Player p) {
        int count = db.countItems(p.getUniqueId().toString());
        if (count <= 0)
            return;

        String msg = messages.getString(
                "mailbox.remindermsg.mail_reminder",
                "&aYou have %amount% unclaimed rewards. Use /mail to claim them."
        );

        p.sendMessage(msg.replace("%amount%", String.valueOf(count)));

        playMailSound(p);
    }

    private void playMailSound(Player p) {
        String key = "Mailbox.sounds.received";
        String soundName = cfg.getString(key, "ENTITY_EXPERIENCE_ORB_PICKUP");
        if (soundName == null) return;

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            p.playSound(p.getLocation(), sound, 1f, 1f);
        } catch (IllegalArgumentException ignored) {
            // invalid sound name in config: ignore
        }
    }
}
