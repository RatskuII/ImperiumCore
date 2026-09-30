package dev.RatFjc.ImperiumCore.modules.lastdeath;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public final class DeathStore {

    private static final Map<OfflinePlayer, Location> lastDeaths = new HashMap<>();

    public static void cacheLocation(OfflinePlayer player, Location location) {
        lastDeaths.put(player, location);
    }

    public static Location getLastDeath(OfflinePlayer player) {
        if (player == null) throw new NullPointerException("Could not find the player expected for this location store.");
        return lastDeaths.get(player);
    }

    public Location clearLastDeath(OfflinePlayer player) {
        return lastDeaths.remove(player);
    }

    public static void clear() {
        lastDeaths.clear();
    }
}
