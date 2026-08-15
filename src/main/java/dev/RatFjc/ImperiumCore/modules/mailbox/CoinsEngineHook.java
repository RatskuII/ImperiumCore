package dev.RatFjc.ImperiumCore.modules.mailbox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Minimal safe hook for a CoinsEngine-like API.
 * Replace internals with real API calls if you know the API.
 */
public class CoinsEngineHook {

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("CoinsEngine") != null;
    }

    public static double getBalance(UUID player) {
        // TODO: Replace with real CoinsEngine API
        // Example: return CoinsEngineAPI.getBalance(player);
        return Double.MAX_VALUE; // by default treat as infinite if plugin not integrated
    }

    public static boolean withdraw(UUID player, double amount) {
        // TODO: Replace with real API call
        // Example: return CoinsEngineAPI.withdraw(player, amount);
        // If plugin missing, if require-coinsengine is false we'll allow; hook will be checked before calling
        return true;
    }

    public static boolean tryCharge(Player player, double cost, boolean requireEngine) {
        if (player.hasPermission("imperium.mail.admin") || player.isOp()) return true;
        if (!isAvailable()) {
            return !requireEngine; // if not required, treat as success
        }
        // replace with real API logic
        return withdraw(player.getUniqueId(), cost);
    }
}
