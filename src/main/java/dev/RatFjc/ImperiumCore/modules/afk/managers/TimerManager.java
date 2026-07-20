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

    private static final Map<Player, BukkitTask> tasks = new HashMap<>();

    public static void startPreAfkTimer(Player player) {
        if (tasks.get(player) != null) return;
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
                        PDCManager.setAfk(player, true, true);
                    }
                }, 0, 20
        );
        tasks.put(player, task);

        if (!player.isOnline()) {
            task.cancel();
            cancelPreAfkTimer(player);
        }
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
        tasks.get(player).cancel();
        tasks.remove(player);
    }

    public static int getPreAfkTimer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.PRE, PersistentDataType.INTEGER);

        return key != null ? key : 0;
    }

}
