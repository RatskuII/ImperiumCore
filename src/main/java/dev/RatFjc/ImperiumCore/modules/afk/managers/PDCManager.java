package dev.RatFjc.ImperiumCore.modules.afk.managers;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCManager extends AfkManager {

    public static boolean isAfk(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.AFK, PersistentDataType.BOOLEAN);

        return key != null && key;
    }

    public static void setAfk(Player player, boolean setAfkStatus, boolean message) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (isAfk(player) == setAfkStatus) return;

        container.set(Keys.AFK, PersistentDataType.BOOLEAN, setAfkStatus);
        PlayerManager.afkEffects(player, setAfkStatus);
        if (setAfkStatus) {
            container.set(Keys.POST, PersistentDataType.LONG, System.currentTimeMillis());
            if (message) TextUtil.announce(player.getName() + " is now afk.");
            // LogUtil.log("Player " + player.getName() + " is now AFK.", new Afk(), Level.INFO, true);
        } else {
            container.set(Keys.POST, PersistentDataType.LONG, 0L);
            if (message) TextUtil.announce(player.getName() + " is no longer afk.");
            // LogUtil.log("Player " + player.getName() + " is no longer AFK.", new Afk(), Level.INFO, true);
        }
    }

    public static long getDuration(Player player) {
        if (!isAfk(player)) return 0;

        PersistentDataContainer container = player.getPersistentDataContainer();
        var value = container.get(Keys.POST, PersistentDataType.LONG);
        if (value == null) return -1;

        return System.currentTimeMillis() - value;
    }
}
