package dev.RatFjc.ImperiumCore.modules.afk.managers;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.init.Afk;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TimerManager extends AfkManager {

    private static final Map<Player, BukkitTask> afkTasks = new HashMap<>();
    private static final Map<Player, BukkitTask> kickTasks = new HashMap<>();

    public static void startPreAfkTimer(Player player) {
        if (afkTasks.get(player) != null) return;
        if (!player.isOnline()) return;
        PDCManager.setAfk(player, false, false);
        PersistentDataContainer container = player.getPersistentDataContainer();
        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(
                plugin, () -> {
                    var value = getPreAfkTimer(player);
                    if (getPreAfkTimer(player) <= 120 && !PDCManager.isAfk(player)) {
                        value++;
                        container.set(Keys.PRE, PersistentDataType.INTEGER, value);
                        // LogUtil.log("Increased the timer by one! The timer is now " + getPreAfkTimer(player) + " for player " + player.getName(), new Afk(), Level.INFO, true);
                    }

                    if (getPreAfkTimer(player) > 120) {
                        LogUtil.log("Timer maxed for " + player.getName() + ". Trying to set them afk...", new Afk(), Level.INFO, true);
                        cancelPreAfkTimer(player);
                        startKickTimer(player);
                        PDCManager.setAfk(player, true, true);
                    }
                }, 0, 20
        );
        afkTasks.put(player, task);

        if (!player.isOnline()) {
            task.cancel();
            cancelPreAfkTimer(player);
        }
    }

    public static void startKickTimer(Player player) {
        if (kickTasks.get(player) != null) return;
        if (!player.isOnline()) return;
        if (player.hasPermission(kickImmunity)) return;

        PersistentDataContainer container = player.getPersistentDataContainer();
        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(
                plugin, () -> {
                    var value = getPostAfkTimer(player);
                    if (getPostAfkTimer(player) <= 600) {
                        value++;
                        container.set(Keys.KICKTIME, PersistentDataType.INTEGER, value);
                    }

                    if (getPostAfkTimer(player) > 600) {
                        LogUtil.log("Kick timer maxed out for " + player.getName() + ". Attempting to afk kick...", new Afk(), Level.INFO, true);
                        cancelKickTimer(player);
                        PlayerManager.afkKick(player);
                    }
                }, 0, 20
        );
        kickTasks.put(player, task);
    }

    public static void cancelKickTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(Keys.KICKTIME, PersistentDataType.INTEGER, 0);
        var target = kickTasks.get(player);
        if (target != null) {
            var task = kickTasks.get(player);
            if (task != null) task.cancel();
        }
        kickTasks.remove(player);
    }

    public static void resetPreAfkTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(Keys.PRE, PersistentDataType.INTEGER, 0);

        PDCManager.setAfk(player, false, true);
        startPreAfkTimer(player);
    }

    public static void cancelPreAfkTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(Keys.PRE, PersistentDataType.INTEGER, 0);
        var task = afkTasks.get(player);
        if (task != null) task.cancel();
        afkTasks.remove(player);
    }

    public static int getPreAfkTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.PRE, PersistentDataType.INTEGER);

        return key != null ? key : 0;
    }

    public static int getPostAfkTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.KICKTIME, PersistentDataType.INTEGER);

        return key != null ? Math.max(key, 120) : 120;
    }

}
